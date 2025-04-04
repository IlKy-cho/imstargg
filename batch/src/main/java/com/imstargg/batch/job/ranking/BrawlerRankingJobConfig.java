package com.imstargg.batch.job.ranking;

import com.imstargg.batch.job.support.ExceptionAlertJobExecutionListener;
import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.client.brawlstars.response.BrawlerResponse;
import com.imstargg.client.brawlstars.response.PlayerRankingResponse;
import com.imstargg.core.enums.Country;
import com.imstargg.storage.db.core.ranking.BrawlerRankingCollectionEntity;
import com.imstargg.storage.db.core.ranking.BrawlerRankingCollectionJpaRepository;
import com.imstargg.storage.db.core.ranking.RankingEntityPlayer;
import com.imstargg.support.alert.AlertManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.builder.TaskletStepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class BrawlerRankingJobConfig {

    private static final Logger log = LoggerFactory.getLogger(BrawlerRankingJobConfig.class);

    private static final String JOB_NAME = "brawlerRankingJob";
    private static final String STEP_NAME = "brawlerRankingStep";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;

    private final AlertManager alertManager;
    private final BrawlStarsClient brawlStarsClient;
    private final BrawlerRankingCollectionJpaRepository brawlerRankingJpaRepository;

    public BrawlerRankingJobConfig(
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            AlertManager alertManager,
            BrawlStarsClient brawlStarsClient,
            BrawlerRankingCollectionJpaRepository brawlerRankingJpaRepository
    ) {
        this.jobRepository = jobRepository;
        this.txManager = txManager;
        this.alertManager = alertManager;
        this.brawlStarsClient = brawlStarsClient;
        this.brawlerRankingJpaRepository = brawlerRankingJpaRepository;
    }

    @Bean(JOB_NAME)
    Job job() {
        JobBuilder jobBuilder = new JobBuilder(JOB_NAME, jobRepository);
        return jobBuilder
                .start(step())
                .incrementer(new RunIdIncrementer())
                .listener(new ExceptionAlertJobExecutionListener(alertManager))
                .build();
    }

    @Bean(STEP_NAME)
    @JobScope
    Step step() {
        TaskletStepBuilder taskletStepBuilder = new TaskletStepBuilder(new StepBuilder(STEP_NAME, jobRepository));
        return taskletStepBuilder.tasklet((contribution, chunkContext) -> {
            List<Long> brawlerBrawlerStarsIds = brawlStarsClient.getBrawlers().items().stream()
                    .map(BrawlerResponse::id).toList();
            for (Country country : Country.values()) {
                for (long brawlerBrawlerStarsId : brawlerBrawlerStarsIds) {
                    log.debug("processing country={}, brawlerId={}", country, brawlerBrawlerStarsId);
                    List<PlayerRankingResponse> brawlerRankingResponseList = brawlStarsClient
                            .getBrawlerRanking(country.getCode(), brawlerBrawlerStarsId).items().stream()
                            .sorted(Comparator.comparingInt(PlayerRankingResponse::rank))
                            .toList();
                    List<BrawlerRankingCollectionEntity> brawlerRankingEntities = new ArrayList<>(
                            brawlerRankingJpaRepository
                                    .findAllByCountryAndBrawlerBrawlStarsId(country, brawlerBrawlerStarsId)
                                    .stream()
                                    .sorted(Comparator.comparingInt(BrawlerRankingCollectionEntity::getRank))
                                    .toList()
                    );
                    Map<String, Integer> tagToPrevRank = brawlerRankingEntities.stream()
                            .collect(Collectors.toMap(
                                    entity -> entity.getPlayer().getBrawlStarsTag(),
                                    BrawlerRankingCollectionEntity::getRank
                            ));

                    for (int i = 0; i < brawlerRankingResponseList.size(); i++) {
                        var response = brawlerRankingResponseList.get(i);
                        var entityPlayer = new RankingEntityPlayer(
                                response.tag(),
                                response.name(),
                                response.nameColor(),
                                response.icon().id(),
                                response.club() != null ? response.club().name() : null
                        );
                        if (i >= brawlerRankingEntities.size()) {
                            brawlerRankingEntities.add(new BrawlerRankingCollectionEntity(
                                    country,
                                    brawlerBrawlerStarsId,
                                    entityPlayer,
                                    response.trophies(),
                                    response.rank()
                            ));
                        }

                        var entity = brawlerRankingEntities.get(i);
                        entity.update(entityPlayer, response.trophies(), tagToPrevRank.get(entityPlayer.getBrawlStarsTag()));
                    }

                    if (brawlerRankingEntities.size() > brawlerRankingResponseList.size()) {
                        brawlerRankingJpaRepository.deleteAllInBatch(
                                brawlerRankingEntities.subList(brawlerRankingResponseList.size(), brawlerRankingEntities.size())
                        );
                    }

                    brawlerRankingJpaRepository.saveAll(brawlerRankingEntities);
                }
            }
            return RepeatStatus.FINISHED;
        }, txManager).build();
    }
}

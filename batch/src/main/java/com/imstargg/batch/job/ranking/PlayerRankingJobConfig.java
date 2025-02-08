package com.imstargg.batch.job.ranking;

import com.imstargg.batch.job.support.ExceptionAlertJobExecutionListener;
import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.client.brawlstars.response.PlayerRankingResponse;
import com.imstargg.core.enums.Country;
import com.imstargg.storage.db.core.ranking.PlayerRankingCollectionEntity;
import com.imstargg.storage.db.core.ranking.PlayerRankingCollectionJpaRepository;
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

@Configuration
public class PlayerRankingJobConfig {

    private static final Logger log = LoggerFactory.getLogger(PlayerRankingJobConfig.class);

    private static final String JOB_NAME = "playerRankingJob";
    private static final String STEP_NAME = "playerRankingStep";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;

    private final AlertManager alertManager;
    private final BrawlStarsClient brawlStarsClient;
    private final PlayerRankingCollectionJpaRepository playerRankingJpaRepository;

    public PlayerRankingJobConfig(
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            AlertManager alertManager,
            BrawlStarsClient brawlStarsClient,
            PlayerRankingCollectionJpaRepository playerRankingJpaRepository
    ) {
        this.jobRepository = jobRepository;
        this.txManager = txManager;
        this.alertManager = alertManager;
        this.brawlStarsClient = brawlStarsClient;
        this.playerRankingJpaRepository = playerRankingJpaRepository;
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
            for (Country country : Country.values()) {
                log.debug("processing country: {}", country);
                List<PlayerRankingResponse> playerRankingResponseList = brawlStarsClient
                        .getPlayerRanking(country.getCode()).items().stream()
                        .sorted(Comparator.comparingInt(PlayerRankingResponse::rank))
                        .toList();
                List<PlayerRankingCollectionEntity> playerRankingEntities = new ArrayList<>(
                        playerRankingJpaRepository
                                .findAllByCountry(country).stream()
                                .sorted(Comparator.comparingInt(PlayerRankingCollectionEntity::getRank))
                                .toList()
                );

                for (int i = 0; i < playerRankingResponseList.size(); i++) {
                    var response = playerRankingResponseList.get(i);
                    var entityPlayer = new RankingEntityPlayer(
                            response.tag(),
                            response.name(),
                            response.nameColor(),
                            response.icon().id(),
                            response.club() != null ? response.club().name() : null
                    );
                    if (i >= playerRankingEntities.size()) {
                        playerRankingEntities.add(new PlayerRankingCollectionEntity(
                                country,
                                entityPlayer,
                                response.trophies(),
                                response.rank()
                        ));
                    }
                    var entity = playerRankingEntities.get(i);
                    entity.update(entityPlayer, response.trophies());
                }

                if (playerRankingEntities.size() > playerRankingResponseList.size()) {
                    playerRankingJpaRepository.deleteAllInBatch(
                            playerRankingEntities.subList(playerRankingResponseList.size(), playerRankingEntities.size())
                    );
                }

                playerRankingJpaRepository.saveAll(playerRankingEntities);
            }
            return RepeatStatus.FINISHED;
        }, txManager).build();
    }
}

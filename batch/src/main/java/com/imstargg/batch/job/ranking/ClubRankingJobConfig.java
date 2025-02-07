package com.imstargg.batch.job.ranking;

import com.imstargg.batch.job.support.ExceptionAlertJobExecutionListener;
import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.core.enums.Country;
import com.imstargg.storage.db.core.ranking.ClubRankingCollectionEntity;
import com.imstargg.storage.db.core.ranking.ClubRankingCollectionJpaRepository;
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

@Configuration
public class ClubRankingJobConfig {

    private static final Logger log = LoggerFactory.getLogger(ClubRankingJobConfig.class);

    private static final String JOB_NAME = "clubRankingJob";
    private static final String STEP_NAME = "clubRankingStep";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;

    private final AlertManager alertManager;
    private final BrawlStarsClient brawlStarsClient;
    private final ClubRankingCollectionJpaRepository clubRankingJpaRepository;

    public ClubRankingJobConfig(
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            AlertManager alertManager,
            BrawlStarsClient brawlStarsClient,
            ClubRankingCollectionJpaRepository clubRankingJpaRepository
    ) {
        this.jobRepository = jobRepository;
        this.txManager = txManager;
        this.alertManager = alertManager;
        this.brawlStarsClient = brawlStarsClient;
        this.clubRankingJpaRepository = clubRankingJpaRepository;
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
                var entities = brawlStarsClient.getClubRanking(country.getCode()).items()
                        .stream()
                        .map(response -> new ClubRankingCollectionEntity(
                                country,
                                response.tag(),
                                response.name(),
                                response.badgeId(),
                                response.trophies(),
                                response.rank(),
                                response.memberCount()
                        )).toList();

                clubRankingJpaRepository.deleteAllInBatch(
                        clubRankingJpaRepository.findAllByCountry(country));
                clubRankingJpaRepository.saveAll(entities);
            }
            return RepeatStatus.FINISHED;
        }, txManager).build();
    }
}

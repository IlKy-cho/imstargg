package com.imstargg.batch.job;

import com.imstargg.batch.job.support.ExceptionAlertJobExecutionListener;
import com.imstargg.core.enums.PlayerRenewalStatus;
import com.imstargg.storage.db.core.PlayerRenewalCollectionEntity;
import com.imstargg.storage.db.core.PlayerRenewalCollectionJpaRepository;
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

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;

@Configuration
public class PlayerRenewalFailedJobConfig {

    private static final Logger log = LoggerFactory.getLogger(PlayerRenewalFailedJobConfig.class);

    private static final String JOB_NAME = "playerRenewalFailedJob";
    private static final String STEP_NAME = "playerRenewalFailedStep";

    private final Clock clock;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;

    private final AlertManager alertManager;
    private final PlayerRenewalCollectionJpaRepository playerRenewalJpaRepository;

    public PlayerRenewalFailedJobConfig(
            Clock clock,
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            AlertManager alertManager,
            PlayerRenewalCollectionJpaRepository playerRenewalJpaRepository
    ) {
        this.clock = clock;
        this.jobRepository = jobRepository;
        this.txManager = txManager;
        this.alertManager = alertManager;
        this.playerRenewalJpaRepository = playerRenewalJpaRepository;
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
        return taskletStepBuilder
                .tasklet((contribution, chunkContext) -> {
                    OffsetDateTime renewalFailedTime = OffsetDateTime.now(clock).minusMinutes(10);
                    List<PlayerRenewalCollectionEntity> failedRenewals = playerRenewalJpaRepository
                            .findAllByStatusIn(PlayerRenewalStatus.renewingList())
                            .stream()
                            .filter(playerRenewal -> playerRenewal.getUpdatedAt().isBefore(renewalFailedTime))
                            .toList();
                    log.info("총 {}개의 갱신 실패. {}",
                            failedRenewals.size(),
                            failedRenewals.stream().map(PlayerRenewalCollectionEntity::getBrawlStarsTag).toList()
                    );
                    failedRenewals.forEach(PlayerRenewalCollectionEntity::failed);
                    playerRenewalJpaRepository.saveAll(failedRenewals);
                    return RepeatStatus.FINISHED;
                }, txManager)
                .build();
    }
}

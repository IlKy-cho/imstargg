package com.imstargg.batch.job;

import com.imstargg.batch.domain.BattleUpdateApplier;
import com.imstargg.batch.domain.PlayerBattleUpdateResult;
import com.imstargg.batch.job.support.ChunkErrorLogListener;
import com.imstargg.batch.job.support.ChunkSizeJobParameter;
import com.imstargg.batch.job.support.ExceptionAlertJobExecutionListener;
import com.imstargg.batch.job.support.QuerydslZeroPagingItemReader;
import com.imstargg.batch.job.support.RunTimestampIncrementer;
import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.core.enums.PlayerStatus;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import com.imstargg.support.alert.AlertManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.OptimisticLockException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.Clock;

import static com.imstargg.storage.db.core.QPlayerCollectionEntity.playerCollectionEntity;

@Configuration
public class BattleUpdateJobConfig {

    private static final String JOB_NAME = "battleUpdateJob";
    private static final String STEP_NAME = "battleUpdateStep";

    private final Clock clock;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;
    private final EntityManagerFactory emf;

    private final AlertManager alertManager;
    private final BrawlStarsClient brawlStarsClient;
    private final BattleUpdateApplier battleUpdateApplier;

    public BattleUpdateJobConfig(
            Clock clock,
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            EntityManagerFactory emf,
            AlertManager alertManager,
            BrawlStarsClient brawlStarsClient,
            BattleUpdateApplier battleUpdateApplier
    ) {
        this.clock = clock;
        this.jobRepository = jobRepository;
        this.txManager = txManager;
        this.emf = emf;
        this.alertManager = alertManager;
        this.brawlStarsClient = brawlStarsClient;
        this.battleUpdateApplier = battleUpdateApplier;
    }

    @Bean(JOB_NAME)
    Job job() {
        JobBuilder jobBuilder = new JobBuilder(JOB_NAME, jobRepository);
        return jobBuilder
                .incrementer(new RunTimestampIncrementer(clock))
                .start(step())
                .listener(new ExceptionAlertJobExecutionListener(alertManager))
                .build();
    }

    @Bean(JOB_NAME + "ChunkSizeJobParameter")
    @JobScope
    ChunkSizeJobParameter chunkSizeJobParameter() {
        return new ChunkSizeJobParameter(10);
    }

    @Bean(STEP_NAME)
    @JobScope
    Step step() {
        StepBuilder stepBuilder = new StepBuilder(STEP_NAME, jobRepository);
        return stepBuilder
                .<PlayerCollectionEntity, PlayerBattleUpdateResult>chunk(chunkSizeJobParameter().getSize(), txManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())

                .faultTolerant()
                .skip(OptimisticLockException.class)
                .listener(new ChunkErrorLogListener())

                .build();
    }

    @Bean(STEP_NAME + "ItemReader")
    @StepScope
    QuerydslZeroPagingItemReader<PlayerCollectionEntity> reader() {
        QuerydslZeroPagingItemReader<PlayerCollectionEntity> reader = new QuerydslZeroPagingItemReader<>(emf, chunkSizeJobParameter().getSize(), queryFactory ->
                queryFactory
                        .selectFrom(playerCollectionEntity)
                        .where(
                                playerCollectionEntity.status.in(PlayerStatus.PLAYER_UPDATED, PlayerStatus.NEW)
                        )
                        .orderBy(playerCollectionEntity.updateWeight.asc())
        );
        reader.setTransacted(false);
        return reader;
    }

    @Bean(STEP_NAME + "ItemProcessor")
    @StepScope
    BattleUpdateJobItemProcessor processor() {
        return new BattleUpdateJobItemProcessor(clock, brawlStarsClient, battleUpdateApplier);
    }

    @Bean(STEP_NAME + "ItemWriter")
    @StepScope
    PlayerBattleUpdateItemWriter writer() {
        return new PlayerBattleUpdateItemWriter(emf);
    }
}

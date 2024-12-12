package com.imstargg.batch.job;

import com.imstargg.batch.domain.PlayerBattleUpdateResult;
import com.imstargg.batch.job.support.ExceptionAlertJobExecutionListener;
import com.imstargg.batch.job.support.QuerydslZeroPagingItemReader;
import com.imstargg.batch.job.support.RunTimestampIncrementer;
import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.collection.domain.BattleUpdateApplier;
import com.imstargg.core.enums.PlayerStatus;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import com.imstargg.support.alert.AlertManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.OptimisticLockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.Chunk;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.Clock;
import java.util.concurrent.Future;

import static com.imstargg.storage.db.core.QPlayerCollectionEntity.playerCollectionEntity;

@Configuration
public class BattleUpdateJobConfig {

    private static final Logger log = LoggerFactory.getLogger(BattleUpdateJobConfig.class);

    private static final String JOB_NAME = "battleUpdateJob";
    private static final String STEP_NAME = "battleUpdateStep";
    private static final int CHUNK_SIZE = 10;

    private final Clock clock;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;
    private final EntityManagerFactory emf;
    private final TaskExecutor taskExecutor;

    private final AlertManager alertManager;
    private final BrawlStarsClient brawlStarsClient;
    private final BattleUpdateApplier battleUpdateApplier;

    public BattleUpdateJobConfig(
            Clock clock,
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            EntityManagerFactory emf,
            TaskExecutor taskExecutor,
            AlertManager alertManager,
            BrawlStarsClient brawlStarsClient,
            BattleUpdateApplier battleUpdateApplier
    ) {
        this.clock = clock;
        this.jobRepository = jobRepository;
        this.txManager = txManager;
        this.emf = emf;
        this.taskExecutor = taskExecutor;
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

    @Bean(STEP_NAME)
    @JobScope
    Step step() {
        StepBuilder stepBuilder = new StepBuilder(STEP_NAME, jobRepository);
        return stepBuilder
                .<PlayerCollectionEntity, Future<PlayerBattleUpdateResult>>chunk(CHUNK_SIZE, txManager)
                .reader(reader())
                .processor(asyncProcessor())
                .writer(asyncWriter())

                .faultTolerant()
                .skipLimit(3)
                .skip(OptimisticLockException.class)
                .listener(new ItemWriteListener<>() {
                    @Override
                    public void onWriteError(Exception exception, Chunk<? extends Future<PlayerBattleUpdateResult>> items) {
                        log.warn("{} 중 플레이어 업데이트 충돌 발생.",
                                JOB_NAME,
                                exception
                        );
                    }
                })

                .build();
    }

    @Bean(STEP_NAME + "ItemReader")
    @StepScope
    QuerydslZeroPagingItemReader<PlayerCollectionEntity> reader() {
        QuerydslZeroPagingItemReader<PlayerCollectionEntity> reader = new QuerydslZeroPagingItemReader<>(
                emf, CHUNK_SIZE, queryFactory ->
                queryFactory
                        .selectFrom(playerCollectionEntity)
                        .where(
                                playerCollectionEntity.status.in(
                                        PlayerStatus.PLAYER_UPDATED, PlayerStatus.BATTLE_UPDATED, PlayerStatus.NEW
                                )
                        )
                        .orderBy(playerCollectionEntity.updateWeight.asc())
        );
        reader.setTransacted(false);
        reader.setSaveState(false);
        return reader;
    }

    @Bean(STEP_NAME + "AsyncItemProcessor")
    @StepScope
    AsyncItemProcessor<PlayerCollectionEntity, PlayerBattleUpdateResult> asyncProcessor() {
        AsyncItemProcessor<PlayerCollectionEntity, PlayerBattleUpdateResult> asyncProcessor = new AsyncItemProcessor<>();
        asyncProcessor.setDelegate(processor());
        asyncProcessor.setTaskExecutor(taskExecutor);
        return asyncProcessor;
    }

    @Bean(STEP_NAME + "ItemProcessor")
    @StepScope
    BattleUpdateJobItemProcessor processor() {
        return new BattleUpdateJobItemProcessor(clock, brawlStarsClient, battleUpdateApplier);
    }

    @Bean(STEP_NAME + "AsyncItemWriter")
    @StepScope
    AsyncItemWriter<PlayerBattleUpdateResult> asyncWriter() {
        AsyncItemWriter<PlayerBattleUpdateResult> asyncWriter = new AsyncItemWriter<>();
        asyncWriter.setDelegate(writer());
        return asyncWriter;
    }

    @Bean(STEP_NAME + "ItemWriter")
    @StepScope
    PlayerBattleUpdateItemWriter writer() {
        return new PlayerBattleUpdateItemWriter(emf);
    }
}

package com.imstargg.batch.job;

import com.imstargg.batch.domain.PlayerBattleUpdateResult;
import com.imstargg.batch.job.support.ExceptionAlertJobExecutionListener;
import com.imstargg.batch.job.support.PeriodDateTimeJobParameter;
import com.imstargg.batch.job.support.PlayerStatusJobParameter;
import com.imstargg.batch.job.support.querydsl.QuerydslZeroPagingItemReader;
import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.collection.domain.BattleUpdateApplier;
import com.imstargg.core.enums.PlayerStatus;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import com.imstargg.support.alert.AlertManager;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.OptimisticLockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.Chunk;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.Clock;
import java.util.concurrent.Future;

import static com.imstargg.storage.db.core.QPlayerCollectionEntity.playerCollectionEntity;

@Configuration
public class BattleUpdateJobConfig {

    private static final Logger log = LoggerFactory.getLogger(BattleUpdateJobConfig.class);

    private static final String JOB_NAME = "battleUpdateJob";
    private static final String STEP_NAME = "battleUpdateStep";

    private final int chunkSize;
    private final Clock clock;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;
    private final EntityManagerFactory emf;
    private final TaskExecutor taskExecutor;

    private final AlertManager alertManager;
    private final BrawlStarsClient brawlStarsClient;
    private final BattleUpdateApplier battleUpdateApplier;

    public BattleUpdateJobConfig(
            @Value("${app.batch.battleUpdateJob.chunk-size}") int chunkSize,
            Clock clock,
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            EntityManagerFactory emf,
            TaskExecutor taskExecutor,
            AlertManager alertManager,
            BrawlStarsClient brawlStarsClient,
            BattleUpdateApplier battleUpdateApplier
    ) {
        this.chunkSize = chunkSize;
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
                .validator(new DefaultJobParametersValidator(
                        new String[]{},
                        new String[]{"player.status", "period.from", "period.to"}
                ))
                .start(step())
                .listener(new ExceptionAlertJobExecutionListener(alertManager))
                .build();
    }

    @Bean(JOB_NAME + "PeriodDateTimeJobParameter")
    @JobScope
    PeriodDateTimeJobParameter periodDateTimeJobParameter() {
        return new PeriodDateTimeJobParameter();
    }

    @Bean(JOB_NAME + "PlayerStatusJobParameter")
    @JobScope
    PlayerStatusJobParameter playerStatusJobParameter() {
        return new PlayerStatusJobParameter();
    }

    @Bean(STEP_NAME)
    @JobScope
    Step step() {
        StepBuilder stepBuilder = new StepBuilder(STEP_NAME, jobRepository);
        return stepBuilder
                .<PlayerCollectionEntity, Future<PlayerBattleUpdateResult>>chunk(chunkSize, txManager)
                .reader(reader())
                .processor(asyncProcessor())
                .writer(asyncWriter())

                .faultTolerant()
                .backOffPolicy(new FixedBackOffPolicy())
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
                emf, chunkSize, queryFactory ->
                queryFactory
                        .selectFrom(playerCollectionEntity)
                        .where(
                                playerStatusCondition(),
                                nextUpdateTimeGoe(),
                                nextUpdateTimeLt()
                        )
                        .orderBy(playerCollectionEntity.nextUpdateTime.asc())
        );
        reader.setTransacted(false);
        reader.setSaveState(false);
        return reader;
    }

    private BooleanExpression playerStatusCondition() {
        if (playerStatusJobParameter().getStatus() != null) {
            return playerCollectionEntity.status.eq(playerStatusJobParameter().getStatus());
        }

        return playerCollectionEntity.status.in(
                PlayerStatus.PLAYER_UPDATED, PlayerStatus.BATTLE_UPDATED, PlayerStatus.NEW
        );
    }

    private BooleanExpression nextUpdateTimeGoe() {
        if (periodDateTimeJobParameter().getFrom() == null) {
            return null;
        }

        return playerCollectionEntity.nextUpdateTime.goe(periodDateTimeJobParameter().getFrom());
    }

    private BooleanExpression nextUpdateTimeLt() {
        if (periodDateTimeJobParameter().getTo() == null) {
            return null;
        }

        return playerCollectionEntity.nextUpdateTime.lt(periodDateTimeJobParameter().getTo());
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

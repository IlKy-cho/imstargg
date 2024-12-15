package com.imstargg.batch.job;

import com.imstargg.batch.job.support.ExceptionLoggingJobExecutionListener;
import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.core.enums.PlayerStatus;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
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
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.concurrent.Future;

import static com.imstargg.storage.db.core.QPlayerCollectionEntity.playerCollectionEntity;

@Configuration
public class PlayerUpdateJobConfig {

    private static final Logger log = LoggerFactory.getLogger(PlayerUpdateJobConfig.class);

    private static final String JOB_NAME = "playerUpdateJob";
    private static final String STEP_NAME = "playerUpdateStep";

    private final int chunkSize;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;
    private final EntityManagerFactory emf;
    private final TaskExecutor taskExecutor;

    private final BrawlStarsClient brawlStarsClient;

    PlayerUpdateJobConfig(
            @Value("${app.batch.playerUpdateJob.chunk-size}") int chunkSize,
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            EntityManagerFactory emf,
            TaskExecutor taskExecutor,
            BrawlStarsClient brawlStarsClient
    ) {
        this.chunkSize = chunkSize;
        this.jobRepository = jobRepository;
        this.txManager = txManager;
        this.emf = emf;
        this.taskExecutor = taskExecutor;
        this.brawlStarsClient = brawlStarsClient;
    }

    @Bean(JOB_NAME)
    Job job() {
        JobBuilder jobBuilder = new JobBuilder(JOB_NAME, jobRepository);
        return jobBuilder
                .incrementer(new RunIdIncrementer())
                .start(step())
                .listener(new ExceptionLoggingJobExecutionListener())
                .build();
    }

    @Bean(STEP_NAME)
    @JobScope
    Step step() {
        StepBuilder stepBuilder = new StepBuilder(STEP_NAME, jobRepository);
        return stepBuilder
                .<PlayerCollectionEntity, Future<PlayerCollectionEntity>>chunk(chunkSize, txManager)
                .reader(reader())
                .processor(asyncProcessor())
                .writer(asyncWriter())

                .faultTolerant()
                .backOffPolicy(new FixedBackOffPolicy())
                .skipLimit(3)
                .skip(OptimisticLockException.class)
                .listener(new ItemWriteListener<>() {
                    @Override
                    public void onWriteError(Exception exception, Chunk<? extends Future<PlayerCollectionEntity>> items) {
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
    PlayerUpdateJobItemReader reader() {
        return new PlayerUpdateJobItemReader(emf, chunkSize, queryFactory ->
                queryFactory
                        .selectFrom(playerCollectionEntity)
                        .where(
                                playerCollectionEntity.status.eq(PlayerStatus.BATTLE_UPDATED)
                        )
        );
    }

    @Bean(STEP_NAME + "AsyncItemProcessor")
    @StepScope
    AsyncItemProcessor<PlayerCollectionEntity, PlayerCollectionEntity> asyncProcessor() {
        AsyncItemProcessor<PlayerCollectionEntity, PlayerCollectionEntity> asyncProcessor = new AsyncItemProcessor<>();
        asyncProcessor.setDelegate(processor());
        asyncProcessor.setTaskExecutor(taskExecutor);
        return asyncProcessor;
    }

    @Bean(STEP_NAME + "ItemProcessor")
    @StepScope
    PlayerUpdateJobItemProcessor processor() {
        return new PlayerUpdateJobItemProcessor(brawlStarsClient);
    }

    @Bean(STEP_NAME + "AsyncItemWriter")
    @StepScope
    AsyncItemWriter<PlayerCollectionEntity> asyncWriter() {
        AsyncItemWriter<PlayerCollectionEntity> asyncWriter = new AsyncItemWriter<>();
        asyncWriter.setDelegate(writer());
        return asyncWriter;
    }

    @Bean(STEP_NAME + "ItemWriter")
    @StepScope
    JpaItemWriter<PlayerCollectionEntity> writer() {
        return new JpaItemWriterBuilder<PlayerCollectionEntity>()
                .entityManagerFactory(emf)
                .usePersist(false)
                .build();
    }
}

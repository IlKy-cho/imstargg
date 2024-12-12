package com.imstargg.batch.job;

import com.imstargg.batch.job.support.ExceptionLoggingJobExecutionListener;
import com.imstargg.batch.job.support.QuerydslZeroPagingItemReader;
import com.imstargg.batch.job.support.RunTimestampIncrementer;
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
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.Clock;

import static com.imstargg.storage.db.core.QPlayerCollectionEntity.playerCollectionEntity;

@Configuration
public class PlayerUpdateJobConfig {

    private static final Logger log = LoggerFactory.getLogger(PlayerUpdateJobConfig.class);

    private static final String JOB_NAME = "playerUpdateJob";
    private static final String STEP_NAME = "playerUpdateStep";
    private static final int CHUNK_SIZE = 10;

    private final Clock clock;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;
    private final EntityManagerFactory emf;

    private final BrawlStarsClient brawlStarsClient;

    PlayerUpdateJobConfig(
            Clock clock,
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            EntityManagerFactory emf,
            BrawlStarsClient brawlStarsClient
    ) {
        this.clock = clock;
        this.jobRepository = jobRepository;
        this.txManager = txManager;
        this.emf = emf;
        this.brawlStarsClient = brawlStarsClient;
    }

    @Bean(JOB_NAME)
    Job job() {
        JobBuilder jobBuilder = new JobBuilder(JOB_NAME, jobRepository);
        return jobBuilder
                .incrementer(new RunTimestampIncrementer(clock))
                .start(step())
                .listener(new ExceptionLoggingJobExecutionListener())
                .build();
    }

    @Bean(STEP_NAME)
    @JobScope
    Step step() {
        StepBuilder stepBuilder = new StepBuilder(STEP_NAME, jobRepository);
        return stepBuilder
                .<PlayerCollectionEntity, PlayerCollectionEntity>chunk(CHUNK_SIZE, txManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())

                .faultTolerant()
                .backOffPolicy(new FixedBackOffPolicy())
                .skipLimit(3)
                .skip(OptimisticLockException.class)
                .listener(new ItemWriteListener<>() {
                    @Override
                    public void onWriteError(Exception exception, Chunk<? extends PlayerCollectionEntity> items) {
                        log.warn("{} 중 플레이어 업데이트 충돌 발생. items={}",
                                JOB_NAME,
                                items.getItems().stream().map(PlayerCollectionEntity::getBrawlStarsTag).toList(),
                                exception
                        );
                    }
                })

                .build();
    }

    @Bean(STEP_NAME + "ItemReader")
    @StepScope
    QuerydslZeroPagingItemReader<PlayerCollectionEntity> reader() {
        return new QuerydslZeroPagingItemReader<>(emf, CHUNK_SIZE, queryFactory ->
                queryFactory
                        .selectFrom(playerCollectionEntity)
                        .where(
                                playerCollectionEntity.status.eq(PlayerStatus.BATTLE_UPDATED)
                        )
        );
    }

    @Bean(STEP_NAME + "ItemProcessor")
    @StepScope
    PlayerUpdateJobItemProcessor processor() {
        return new PlayerUpdateJobItemProcessor(brawlStarsClient);
    }

    @Bean(STEP_NAME + "ItemWriter")
    @StepScope
    ItemWriter<PlayerCollectionEntity> writer() {
        return chunk -> {
        };
    }
}

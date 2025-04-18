package com.imstargg.batch.job;

import com.imstargg.batch.domain.PlayerBattleUpdateResult;
import com.imstargg.batch.job.support.ExceptionLoggingJobExecutionListener;
import com.imstargg.collection.domain.PlayerUpdaterFactory;
import com.imstargg.storage.db.core.player.PlayerCollectionEntity;
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
import org.springframework.batch.item.Chunk;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableConfigurationProperties(PlayerUpdateJobConfig.Properties.class)
class PlayerUpdateJobConfig {

    private static final Logger log = LoggerFactory.getLogger(PlayerUpdateJobConfig.class);

    private static final String JOB_NAME = "playerUpdateJob";
    private static final String STEP_NAME = "playerUpdateStep";

    private final Properties properties;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;
    private final EntityManagerFactory emf;

    private final PlayerUpdaterFactory playerUpdaterFactory;

    PlayerUpdateJobConfig(
            Properties properties,
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            EntityManagerFactory emf,
            PlayerUpdaterFactory playerUpdaterFactory
    ) {
        this.properties = properties;
        this.jobRepository = jobRepository;
        this.txManager = txManager;
        this.emf = emf;
        this.playerUpdaterFactory = playerUpdaterFactory;
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
                .<PlayerCollectionEntity, PlayerBattleUpdateResult>chunk(properties.chunkSize(), txManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())

                .faultTolerant()
                .backOffPolicy(new FixedBackOffPolicy())
                .skipLimit(3)
                .skip(OptimisticLockException.class)
                .listener(new ItemWriteListener<>() {
                    @Override
                    public void onWriteError(Exception exception, Chunk<? extends PlayerBattleUpdateResult> items) {
                        log.warn("{} 중 플레이어 업데이트 충돌 발생. tags={}",
                                JOB_NAME,
                                items.getItems().stream()
                                        .map(PlayerBattleUpdateResult::playerEntity)
                                        .map(PlayerCollectionEntity::getBrawlStarsTag).toList(),
                                exception
                        );
                    }
                })

                .build();
    }

    @Bean(STEP_NAME + "ItemReader")
    @StepScope
    PlayerUpdateJobItemReader reader() {
        return new PlayerUpdateJobItemReader(emf, properties.chunkSize());
    }

    @Bean(STEP_NAME + "ItemProcessor")
    @StepScope
    PlayerUpdateProcessor processor() {
        return new PlayerUpdateProcessor(playerUpdaterFactory);
    }

    @Bean(STEP_NAME + "ItemWriter")
    @StepScope
    PlayerBattleUpdateItemWriter writer() {
        return new PlayerBattleUpdateItemWriter(emf);
    }

    @ConfigurationProperties(prefix = "app.batch.player-update-job")
    record Properties(
            int chunkSize
    ) {
    }
}

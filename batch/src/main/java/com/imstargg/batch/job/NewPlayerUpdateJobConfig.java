package com.imstargg.batch.job;

import com.imstargg.batch.domain.NewPlayer;
import com.imstargg.batch.job.support.ExceptionAlertJobExecutionListener;
import com.imstargg.batch.job.support.querydsl.QuerydslZeroPagingItemReader;
import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.core.enums.UnknownPlayerStatus;
import com.imstargg.storage.db.core.UnknownPlayerCollectionEntity;
import com.imstargg.support.alert.AlertManager;
import jakarta.persistence.EntityManagerFactory;
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
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.Chunk;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.Clock;
import java.util.concurrent.Future;

import static com.imstargg.storage.db.core.QUnknownPlayerCollectionEntity.unknownPlayerCollectionEntity;

@Configuration
@EnableConfigurationProperties(NewPlayerUpdateJobConfig.Properties.class)
class NewPlayerUpdateJobConfig {

    private static final Logger log = LoggerFactory.getLogger(NewPlayerUpdateJobConfig.class);

    private static final String JOB_NAME = "newPlayerUpdateJob";
    private static final String STEP_NAME = "newPlayerUpdateStep";

    private final NewPlayerUpdateJobConfig.Properties properties;
    private final Clock clock;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;
    private final EntityManagerFactory emf;
    private final TaskExecutor taskExecutor;

    private final AlertManager alertManager;
    private final BrawlStarsClient brawlStarsClient;

    NewPlayerUpdateJobConfig(
            NewPlayerUpdateJobConfig.Properties properties,
            Clock clock,
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            EntityManagerFactory emf,
            TaskExecutor taskExecutor,
            AlertManager alertManager,
            BrawlStarsClient brawlStarsClient
    ) {
        this.properties = properties;
        this.clock = clock;
        this.jobRepository = jobRepository;
        this.txManager = txManager;
        this.emf = emf;
        this.taskExecutor = taskExecutor;
        this.alertManager = alertManager;
        this.brawlStarsClient = brawlStarsClient;
    }

    @Bean(JOB_NAME)
    Job job() {
        JobBuilder jobBuilder = new JobBuilder(JOB_NAME, jobRepository);
        return jobBuilder
                .incrementer(new RunIdIncrementer())
                .start(step())
                .listener(new ExceptionAlertJobExecutionListener(alertManager))
                .build();
    }

    @Bean(STEP_NAME)
    @JobScope
    Step step() {
        StepBuilder stepBuilder = new StepBuilder(STEP_NAME, jobRepository);
        if (properties.asyncEnabled()) {
            return configureFaultTolerantAndBuild(stepBuilder
                    .<UnknownPlayerCollectionEntity, Future<NewPlayer>>chunk(properties.chunkSize(), txManager)
                    .reader(reader())
                    .processor(asyncProcessor())
                    .writer(asyncWriter())
            );
        } else {
            return configureFaultTolerantAndBuild(stepBuilder
                    .<UnknownPlayerCollectionEntity, NewPlayer>chunk(properties.chunkSize(), txManager)
                    .reader(reader())
                    .processor(processor())
                    .writer(writer())
            );
        }
    }

    private <T> Step configureFaultTolerantAndBuild(
            SimpleStepBuilder<UnknownPlayerCollectionEntity, T> stepBuilder) {
        return stepBuilder
                .listener(new ItemWriteListener<>() {
                    @Override
                    public void onWriteError(Exception exception, Chunk<? extends T> items) {
                        log.warn("{} 중 중복된 플레이어 저장 발생.",
                                JOB_NAME,
                                exception
                        );
                    }
                })
                .faultTolerant()
                .skip(SQLIntegrityConstraintViolationException.class)
                .build();
    }

    @Bean(STEP_NAME + "ItemReader")
    @StepScope
    QuerydslZeroPagingItemReader<UnknownPlayerCollectionEntity> reader() {
        QuerydslZeroPagingItemReader<UnknownPlayerCollectionEntity> reader = new QuerydslZeroPagingItemReader<>(
                emf, properties.chunkSize(), queryFactory ->
                queryFactory
                        .selectFrom(unknownPlayerCollectionEntity)
                        .where(unknownPlayerCollectionEntity.status.in(
                                UnknownPlayerStatus.UPDATE_NEW, UnknownPlayerStatus.ADMIN_NEW
                        ))
        );
        reader.setTransacted(false);
        reader.setSaveState(false);
        return reader;
    }

    @Bean(STEP_NAME + "AsyncItemProcessor")
    @StepScope
    AsyncItemProcessor<UnknownPlayerCollectionEntity, NewPlayer> asyncProcessor() {
        AsyncItemProcessor<UnknownPlayerCollectionEntity, NewPlayer> asyncProcessor = new AsyncItemProcessor<>();
        asyncProcessor.setDelegate(processor());
        asyncProcessor.setTaskExecutor(taskExecutor);
        return asyncProcessor;
    }

    @Bean(STEP_NAME + "ItemProcessor")
    @StepScope
    NewPlayerUpdateJobItemProcessor processor() {
        return new NewPlayerUpdateJobItemProcessor(brawlStarsClient);
    }


    @Bean(STEP_NAME + "AsyncItemWriter")
    @StepScope
    AsyncItemWriter<NewPlayer> asyncWriter() {
        AsyncItemWriter<NewPlayer> asyncWriter = new AsyncItemWriter<>();
        asyncWriter.setDelegate(writer());
        return asyncWriter;
    }

    @Bean(STEP_NAME + "ItemWriter")
    @StepScope
    NewPlayerUpdateJobItemWriter writer() {
        return new NewPlayerUpdateJobItemWriter(emf);
    }

    @ConfigurationProperties(prefix = "app.batch.new-player-update-job")
    record Properties(
            int chunkSize,
            boolean asyncEnabled
    ) {
    }
}

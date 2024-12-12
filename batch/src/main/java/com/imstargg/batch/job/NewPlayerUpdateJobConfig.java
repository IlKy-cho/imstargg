package com.imstargg.batch.job;

import com.imstargg.batch.domain.NewPlayer;
import com.imstargg.batch.job.support.ExceptionAlertJobExecutionListener;
import com.imstargg.batch.job.support.QuerydslZeroPagingItemReader;
import com.imstargg.batch.job.support.RunTimestampIncrementer;
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
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.Chunk;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.Clock;
import java.util.concurrent.Future;

import static com.imstargg.storage.db.core.QUnknownPlayerCollectionEntity.unknownPlayerCollectionEntity;

@Configuration
public class NewPlayerUpdateJobConfig {

    private static final Logger log = LoggerFactory.getLogger(NewPlayerUpdateJobConfig.class);

    private static final String JOB_NAME = "newPlayerUpdateJob";
    private static final String STEP_NAME = "newPlayerUpdateStep";
    private static final int CHUNK_SIZE = 10;

    private final Clock clock;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;
    private final EntityManagerFactory emf;
    private final TaskExecutor taskExecutor;

    private final AlertManager alertManager;
    private final BrawlStarsClient brawlStarsClient;

    NewPlayerUpdateJobConfig(
            Clock clock,
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            EntityManagerFactory emf,
            TaskExecutor taskExecutor,
            AlertManager alertManager,
            BrawlStarsClient brawlStarsClient
    ) {
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
                .<UnknownPlayerCollectionEntity, Future<NewPlayer>>chunk(CHUNK_SIZE, txManager)
                .reader(reader())
                .processor(asyncProcessor())
                .writer(asyncWriter())

                .faultTolerant()
                .skip(SQLIntegrityConstraintViolationException.class)
                .listener(new ItemWriteListener<>() {
                    @Override
                    public void onWriteError(Exception exception, Chunk<? extends Future<NewPlayer>> items) {
                        log.warn("{} 중 중복된 플레이어 저장 발생.",
                                JOB_NAME,
                                exception
                        );
                    }
                })

                .build();
    }

    @Bean(STEP_NAME + "ItemReader")
    @StepScope
    QuerydslZeroPagingItemReader<UnknownPlayerCollectionEntity> reader() {
        QuerydslZeroPagingItemReader<UnknownPlayerCollectionEntity> reader = new QuerydslZeroPagingItemReader<>(
                emf, CHUNK_SIZE, queryFactory ->
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
        return new NewPlayerUpdateJobItemProcessor(clock, brawlStarsClient);
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
}

package com.imstargg.batch.job;

import com.imstargg.batch.domain.NewPlayer;
import com.imstargg.batch.job.support.ChunkSizeJobParameter;
import com.imstargg.batch.job.support.ExceptionAlertJobExecutionListener;
import com.imstargg.batch.job.support.QuerydslZeroPagingItemReader;
import com.imstargg.batch.job.support.RunTimestampIncrementer;
import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.core.enums.UnknownPlayerStatus;
import com.imstargg.storage.db.core.UnknownPlayerCollectionEntity;
import com.imstargg.support.alert.AlertManager;
import jakarta.persistence.EntityManagerFactory;
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

import static com.imstargg.storage.db.core.QUnknownPlayerCollectionEntity.unknownPlayerCollectionEntity;

@Configuration
public class NewPlayerUpdateJobConfig {

    private static final String JOB_NAME = "newPlayerUpdateJob";
    private static final String STEP_NAME = "newPlayerUpdateStep";

    private final Clock clock;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;
    private final EntityManagerFactory emf;

    private final AlertManager alertManager;
    private final BrawlStarsClient brawlStarsClient;

    NewPlayerUpdateJobConfig(
            Clock clock,
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            EntityManagerFactory emf,
            AlertManager alertManager,
            BrawlStarsClient brawlStarsClient
    ) {
        this.clock = clock;
        this.jobRepository = jobRepository;
        this.txManager = txManager;
        this.emf = emf;
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
                .<UnknownPlayerCollectionEntity, NewPlayer>chunk(chunkSizeJobParameter().getSize(), txManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())

                .build();
    }

    @Bean(STEP_NAME + "ItemReader")
    @StepScope
    QuerydslZeroPagingItemReader<UnknownPlayerCollectionEntity> reader() {
        QuerydslZeroPagingItemReader<UnknownPlayerCollectionEntity> reader = new QuerydslZeroPagingItemReader<>(
                emf, chunkSizeJobParameter().getSize(), queryFactory ->
                queryFactory
                        .selectFrom(unknownPlayerCollectionEntity)
                        .where(unknownPlayerCollectionEntity.status.in(
                                UnknownPlayerStatus.UPDATE_NEW, UnknownPlayerStatus.ADMIN_NEW
                        ))
        );
        reader.setTransacted(false);
        return reader;
    }

    @Bean(STEP_NAME + "ItemProcessor")
    @StepScope
    NewPlayerUpdateJobItemProcessor processor() {
        return new NewPlayerUpdateJobItemProcessor(clock, brawlStarsClient);
    }

    @Bean(STEP_NAME + "ItemWriter")
    @StepScope
    NewPlayerUpdateJobItemWriter writer() {
        return new NewPlayerUpdateJobItemWriter(emf);
    }
}

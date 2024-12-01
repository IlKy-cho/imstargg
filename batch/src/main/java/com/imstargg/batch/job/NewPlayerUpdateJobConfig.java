package com.imstargg.batch.job;

import com.imstargg.batch.domain.NewPlayerAppender;
import com.imstargg.batch.domain.NewPlayerRepository;
import com.imstargg.batch.domain.PlayerUpdater;
import com.imstargg.batch.job.support.ExceptionAlertJobExecutionListener;
import com.imstargg.batch.job.support.JpaItemListWriter;
import com.imstargg.batch.job.support.PagingItemReaderAdapter;
import com.imstargg.batch.job.support.RunTimestampIncrementer;
import com.imstargg.client.brawlstars.BrawlStarsClient;
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
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.Clock;
import java.util.List;

@Configuration
public class NewPlayerUpdateJobConfig {

    private static final String JOB_NAME = "newPlayerUpdateJob";
    private static final String STEP_NAME = "newPlayerUpdateStep";
    private static final int CHUNK_SIZE = 10;

    private final Clock clock;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;
    private final EntityManagerFactory emf;

    private final AlertManager alertManager;
    private final BrawlStarsClient brawlStarsClient;
    private final NewPlayerRepository newPlayerRepository;
    private final NewPlayerAppender newPlayerAppender;
    private final PlayerUpdater playerUpdater;

    NewPlayerUpdateJobConfig(
            Clock clock,
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            EntityManagerFactory emf,
            AlertManager alertManager,
            BrawlStarsClient brawlStarsClient,
            NewPlayerRepository newPlayerRepository,
            NewPlayerAppender newPlayerAppender,
            PlayerUpdater playerUpdater
    ) {
        this.clock = clock;
        this.jobRepository = jobRepository;
        this.txManager = txManager;
        this.emf = emf;
        this.alertManager = alertManager;
        this.brawlStarsClient = brawlStarsClient;
        this.newPlayerRepository = newPlayerRepository;
        this.newPlayerAppender = newPlayerAppender;
        this.playerUpdater = playerUpdater;
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
    public Step step() {
        StepBuilder stepBuilder = new StepBuilder(STEP_NAME, jobRepository);
        return stepBuilder
                .<UnknownPlayerCollectionEntity, List<Object>>chunk(CHUNK_SIZE, txManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean(STEP_NAME + "ItemReader")
    @StepScope
    ItemReader<UnknownPlayerCollectionEntity> reader() {
        return new PagingItemReaderAdapter<>((page, size) -> newPlayerRepository.find(size));
    }

    @Bean(STEP_NAME + "ItemReader")
    @StepScope
    NewPlayerUpdateJobItemProcessor processor() {
        return new NewPlayerUpdateJobItemProcessor(brawlStarsClient, newPlayerAppender, playerUpdater);
    }

    @Bean(STEP_NAME + "ItemWriter")
    @StepScope
    JpaItemListWriter<Object> writer() {
        JpaItemWriter<Object> jpaItemWriter = new JpaItemWriterBuilder<>()
                .entityManagerFactory(emf)
                .usePersist(false)
                .build();
        return new JpaItemListWriter<>(jpaItemWriter);
    }
}

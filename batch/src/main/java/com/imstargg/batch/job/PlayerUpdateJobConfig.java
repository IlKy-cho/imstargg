package com.imstargg.batch.job;

import com.imstargg.batch.domain.PlayerDeleter;
import com.imstargg.batch.domain.PlayerToUpdateEntity;
import com.imstargg.batch.domain.PlayerUpdateEntityRepository;
import com.imstargg.batch.domain.PlayerUpdatedEntity;
import com.imstargg.batch.domain.PlayerUpdater;
import com.imstargg.batch.job.support.ChunkSizeJobParameter;
import com.imstargg.batch.job.support.ExceptionAlertJobExecutionListener;
import com.imstargg.batch.job.support.PagingItemReaderAdapter;
import com.imstargg.batch.job.support.RunTimestampIncrementer;
import com.imstargg.client.brawlstars.BrawlStarsClient;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.Clock;

@Configuration
public class PlayerUpdateJobConfig {

    private static final String JOB_NAME = "playerUpdateJob";
    private static final String STEP_NAME = "playerUpdateStep";

    private final Clock clock;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;
    private final EntityManagerFactory emf;

    private final AlertManager alertManager;
    private final BrawlStarsClient brawlStarsClient;
    private final PlayerUpdateEntityRepository playerUpdateEntityRepository;
    private final PlayerUpdater playerUpdater;
    private final PlayerDeleter playerDeleter;

    PlayerUpdateJobConfig(
            Clock clock,
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            EntityManagerFactory emf,
            AlertManager alertManager,
            BrawlStarsClient brawlStarsClient,
            PlayerUpdateEntityRepository playerUpdateEntityRepository,
            PlayerUpdater playerUpdater,
            PlayerDeleter playerDeleter
    ) {
        this.clock = clock;
        this.jobRepository = jobRepository;
        this.txManager = txManager;
        this.emf = emf;
        this.alertManager = alertManager;
        this.brawlStarsClient = brawlStarsClient;
        this.playerUpdateEntityRepository = playerUpdateEntityRepository;
        this.playerUpdater = playerUpdater;
        this.playerDeleter = playerDeleter;
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
                .<PlayerToUpdateEntity, PlayerUpdatedEntity>chunk(chunkSizeJobParameter().getSize(), txManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())

                .faultTolerant()
                .skip(DataIntegrityViolationException.class)
                .listener(new PlayerUpdateWriteSkipListener())

                .build();
    }

    @Bean(STEP_NAME + "ItemReader")
    @StepScope
    ItemReader<PlayerToUpdateEntity> reader() {
        return new PagingItemReaderAdapter<>((page, size) -> playerUpdateEntityRepository.find(size));
    }

    @Bean(STEP_NAME + "ItemProcessor")
    @StepScope
    PlayerUpdateJobItemProcessor processor() {
        return new PlayerUpdateJobItemProcessor(clock, brawlStarsClient, playerUpdater, playerDeleter);
    }

    @Bean(STEP_NAME + "ItemWriter")
    @StepScope
    PlayerUpdatedItemWriter writer() {
        return new PlayerUpdatedItemWriter(emf);
    }
}

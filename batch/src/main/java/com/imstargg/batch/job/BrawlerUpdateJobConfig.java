package com.imstargg.batch.job;

import com.imstargg.batch.domain.BrawlerBrawlStarsKeyRepositoryInMemoryCache;
import com.imstargg.batch.domain.GadgetBrawlStarsKeyRepositoryInMemoryCache;
import com.imstargg.batch.domain.StarPowerBrawlStarsKeyRepositoryInMemoryCache;
import com.imstargg.batch.job.support.ExceptionAlertJobExecutionListener;
import com.imstargg.batch.job.support.RunTimestampIncrementer;
import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.client.brawlstars.response.BrawlerResponse;
import com.imstargg.support.alert.AlertManager;
import com.imstargg.storage.db.core.BrawlerCollectionEntity;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.Clock;

@Configuration
class BrawlerUpdateJobConfig {

    private static final String JOB_NAME = "brawlerUpdateJob";
    private static final String STEP_NAME = "brawlerUpdateStep";
    private static final int CHUNK_SIZE = 10;

    private final Clock clock;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;
    private final EntityManagerFactory emf;

    private final AlertManager alertManager;
    private final BrawlStarsClient brawlStarsClient;
    private final BrawlerBrawlStarsKeyRepositoryInMemoryCache brawlerRepository;
    private final GadgetBrawlStarsKeyRepositoryInMemoryCache gadgetRepository;
    private final StarPowerBrawlStarsKeyRepositoryInMemoryCache starPowerRepository;

    BrawlerUpdateJobConfig(
            Clock clock,
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            EntityManagerFactory emf,
            AlertManager alertManager,
            BrawlStarsClient brawlStarsClient,
            BrawlerBrawlStarsKeyRepositoryInMemoryCache brawlerRepository,
            GadgetBrawlStarsKeyRepositoryInMemoryCache gadgetRepository,
            StarPowerBrawlStarsKeyRepositoryInMemoryCache starPowerRepository
    ) {
        this.clock = clock;
        this.jobRepository = jobRepository;
        this.txManager = txManager;
        this.emf = emf;
        this.alertManager = alertManager;
        this.brawlStarsClient = brawlStarsClient;
        this.brawlerRepository = brawlerRepository;
        this.gadgetRepository = gadgetRepository;
        this.starPowerRepository = starPowerRepository;
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
                .<BrawlerResponse, BrawlerCollectionEntity>chunk(CHUNK_SIZE, txManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean(STEP_NAME + "ItemReader")
    @StepScope
    ListItemReader<BrawlerResponse> reader() {
        return new ListItemReader<>(brawlStarsClient.getBrawlers().items());
    }

    @Bean(STEP_NAME + "ItemProcessor")
    @StepScope
    BrawlerUpdateJobItemProcessor processor() {
        return new BrawlerUpdateJobItemProcessor(
                brawlerRepository,
                gadgetRepository,
                starPowerRepository
        );
    }

    @Bean(STEP_NAME + "ItemWriter")
    @StepScope
    JpaItemWriter<BrawlerCollectionEntity> writer() {
        return new JpaItemWriterBuilder<BrawlerCollectionEntity>()
                .entityManagerFactory(emf)
                .usePersist(false)
                .build();
    }
}

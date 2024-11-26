package com.imstargg.batch.job;

import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.client.brawlstars.response.BrawlerResponse;
import com.imstargg.support.alert.AlertManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.Clock;

@Configuration
public class PlayerUpdateJobConfig {

    private static final String JOB_NAME = "playerUpdateJob";
    private static final String STEP_NAME = "playerUpdateStep";
    private static final int CHUNK_SIZE = 10;

    private final Clock clock;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;
    private final EntityManagerFactory emf;

    private final AlertManager alertManager;
    private final BrawlStarsClient brawlStarsClient;

    PlayerUpdateJobConfig(
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

//    @Bean(JOB_NAME)
//    Job job() {
//        JobBuilder jobBuilder = new JobBuilder(JOB_NAME, jobRepository);
//        return jobBuilder
//                .incrementer(new RunTimestampIncrementer(clock))
//                .start(step())
//                .listener(new ExceptionAlertJobExecutionListener(alertManager))
//                .build();
//    }

    @Bean(STEP_NAME + "ItemReader")
    @StepScope
    ItemReader<BrawlerResponse> reader() {
        // UPDATE_NEW, UPDATE_PENDING, UPDATED
        return null;
    }
}

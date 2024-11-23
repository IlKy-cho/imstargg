package com.imstargg.batch.job;

import com.imstargg.batch.domain.BrawlerBrawlStarsKeyRepositoryInMemoryCache;
import com.imstargg.batch.domain.GadgetBrawlStarsKeyRepositoryInMemoryCache;
import com.imstargg.batch.domain.StarPowerBrawlStarsKeyRepositoryInMemoryCache;
import com.imstargg.client.brawlstars.BrawlStarsClient;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.Clock;

@Configuration
public class PlayerUpdateJob {

    private static final String JOB_NAME = "playerUpdateJob";
    private static final String STEP_NAME = "playerUpdateStep";
    private static final int CHUNK_SIZE = 100;

    private final Clock clock;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;
    private final EntityManagerFactory emf;

    private final BrawlStarsClient brawlStarsClient;
    private final BrawlerBrawlStarsKeyRepositoryInMemoryCache brawlerRepository;
    private final GadgetBrawlStarsKeyRepositoryInMemoryCache gadgetRepository;
    private final StarPowerBrawlStarsKeyRepositoryInMemoryCache starPowerRepository;

    PlayerUpdateJob(
            Clock clock,
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            EntityManagerFactory emf,
            BrawlStarsClient brawlStarsClient,
            BrawlerBrawlStarsKeyRepositoryInMemoryCache brawlerRepository,
            GadgetBrawlStarsKeyRepositoryInMemoryCache gadgetRepository,
            StarPowerBrawlStarsKeyRepositoryInMemoryCache starPowerRepository
    ) {
        this.clock = clock;
        this.jobRepository = jobRepository;
        this.txManager = txManager;
        this.emf = emf;
        this.brawlStarsClient = brawlStarsClient;
        this.brawlerRepository = brawlerRepository;
        this.gadgetRepository = gadgetRepository;
        this.starPowerRepository = starPowerRepository;
    }
}

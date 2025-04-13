package com.imstargg.batch.job.statistics;

import com.imstargg.batch.domain.statistics.BattleItemReaderFactory;
import com.imstargg.batch.domain.statistics.BrawlerBattleRankStatisticsCollectorFactory;
import com.imstargg.batch.domain.statistics.StatisticsCheckPointer;
import com.imstargg.batch.job.support.ExceptionAlertJobExecutionListener;
import com.imstargg.batch.job.support.JpaItemListWriter;
import com.imstargg.storage.db.core.statistics.BrawlerBattleRankStatisticsCollectionEntity;
import com.imstargg.support.alert.AlertManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Configuration
public class BrawlerBattleRankStatisticsJobConfig {

    public static final String JOB_NAME = "brawlerBattleRankStatisticsJob";
    private static final String STEP_NAME = "brawlerBattleRankStatisticsStep";
    private static final int CHUNK_SIZE = 1;

    private final Clock clock;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;
    private final EntityManagerFactory entityManagerFactory;

    private final AlertManager alertManager;
    private final StatisticsCheckPointer statisticsCheckPointer;
    private final BattleItemReaderFactory battleItemReaderFactory;
    private final BrawlerBattleRankStatisticsCollectorFactory collectorFactory;


    public BrawlerBattleRankStatisticsJobConfig(
            Clock clock,
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            EntityManagerFactory entityManagerFactory,
            AlertManager alertManager,
            StatisticsCheckPointer statisticsCheckPointer,
            BattleItemReaderFactory battleItemReaderFactory,
            BrawlerBattleRankStatisticsCollectorFactory collectorFactory
    ) {
        this.clock = clock;
        this.jobRepository = jobRepository;
        this.txManager = txManager;
        this.entityManagerFactory = entityManagerFactory;
        this.alertManager = alertManager;
        this.statisticsCheckPointer = statisticsCheckPointer;
        this.battleItemReaderFactory = battleItemReaderFactory;
        this.collectorFactory = collectorFactory;
    }

    @Bean(JOB_NAME)
    Job job() {
        JobBuilder jobBuilder = new JobBuilder(JOB_NAME, jobRepository);
        return jobBuilder
                .start(step())
                .listener(new ExceptionAlertJobExecutionListener(alertManager))
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean(STEP_NAME)
    @JobScope
    Step step() {
        StepBuilder stepBuilder = new StepBuilder(STEP_NAME, jobRepository);
        return stepBuilder
                .<LocalDate, List<BrawlerBattleRankStatisticsCollectionEntity>>chunk(CHUNK_SIZE, txManager)
                .reader(reader())
                .writer(writer())
                .build();
    }

    @Bean(STEP_NAME + "ItemReader")
    @StepScope
    ListItemReader<LocalDate> reader() {
        return StatisticsJobItemReaderFactoryUtils.create(clock);
    }

    @Bean(STEP_NAME + "ItemProcessor")
    @StepScope
    StatisticsJobItemProcessor<BrawlerBattleRankStatisticsCollectionEntity> processor() {
        return new StatisticsJobItemProcessor<>(
                JOB_NAME,
                statisticsCheckPointer,
                battleItemReaderFactory,
                collectorFactory
        );
    }

    @Bean(STEP_NAME + "ItemWriter")
    @StepScope
    JpaItemListWriter<BrawlerBattleRankStatisticsCollectionEntity> writer() {
        return new JpaItemListWriter<>(
                new JpaItemWriterBuilder<BrawlerBattleRankStatisticsCollectionEntity>()
                        .entityManagerFactory(entityManagerFactory)
                        .usePersist(false)
                        .build()
        );
    }
}

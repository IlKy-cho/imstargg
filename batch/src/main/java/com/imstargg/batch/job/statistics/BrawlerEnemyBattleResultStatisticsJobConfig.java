package com.imstargg.batch.job.statistics;

import com.imstargg.batch.domain.statistics.BrawlerEnemyBattleResultStatisticsCollectorFactory;
import com.imstargg.batch.job.support.DateJobParameter;
import com.imstargg.batch.job.support.ExceptionAlertJobExecutionListener;
import com.imstargg.batch.job.support.JpaItemListWriter;
import com.imstargg.storage.db.core.BattleCollectionJpaRepository;
import com.imstargg.storage.db.core.BattleJpaRepository;
import com.imstargg.storage.db.core.statistics.BrawlerEnemyBattleResultStatisticsCollectionEntity;
import com.imstargg.support.alert.AlertManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
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
import java.util.List;

@Configuration
public class BrawlerEnemyBattleResultStatisticsJobConfig {

    private static final String JOB_NAME = "brawlerEnemyBattleResultStatisticsJob";
    private static final String STEP_NAME = "brawlerEnemyBattleResultStatisticsStep";
    private static final int CHUNK_SIZE = 1;

    private final Clock clock;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;
    private final EntityManagerFactory emf;

    private final AlertManager alertManager;
    private final BattleJpaRepository battleJpaRepository;
    private final BattleCollectionJpaRepository battleCollectionJpaRepository;

    public BrawlerEnemyBattleResultStatisticsJobConfig(
            Clock clock,
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            EntityManagerFactory emf,
            AlertManager alertManager,
            BattleJpaRepository battleJpaRepository,
            BattleCollectionJpaRepository battleCollectionJpaRepository
    ) {
        this.clock = clock;
        this.jobRepository = jobRepository;
        this.txManager = txManager;
        this.emf = emf;
        this.alertManager = alertManager;
        this.battleJpaRepository = battleJpaRepository;
        this.battleCollectionJpaRepository = battleCollectionJpaRepository;
    }

    @Bean(JOB_NAME)
    Job job() {
        JobBuilder jobBuilder = new JobBuilder(JOB_NAME, jobRepository);
        return jobBuilder
                .start(step())
                .incrementer(new RunIdIncrementer())
                .listener(new ExceptionAlertJobExecutionListener(alertManager))
                .validator(new DefaultJobParametersValidator(new String[]{"date"}, new String[]{}))
                .build();
    }

    @Bean(STEP_NAME)
    @JobScope
    Step step() {
        StepBuilder stepBuilder = new StepBuilder(STEP_NAME, jobRepository);
        return stepBuilder
                .<Long, List<BrawlerEnemyBattleResultStatisticsCollectionEntity>>chunk(CHUNK_SIZE, txManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean(JOB_NAME + "DateJobParameter")
    @JobScope
    DateJobParameter dateJobParameter() {
        return new DateJobParameter();
    }

    @Bean(STEP_NAME + "ItemReader")
    @StepScope
    ListItemReader<Long> reader() {
        List<Long> eventIds = battleJpaRepository
                .findAllDistinctEventBrawlStarsIdsByBattleTypeInAndGreaterThanEqualBattleTime(
                        null, null
                );
        return new ListItemReader<>(eventIds);
    }


    @Bean(STEP_NAME + "ItemProcessor")
    @StepScope
    StatisticsJobItemProcessor<BrawlerEnemyBattleResultStatisticsCollectionEntity> processor() {
        var factory = new BrawlerEnemyBattleResultStatisticsCollectorFactory(clock, emf);
        return new StatisticsJobItemProcessor<>(
                factory, battleCollectionJpaRepository, clock, dateJobParameter().getDate()
        );
    }

    @Bean(STEP_NAME + "ItemWriter")
    @StepScope
    JpaItemListWriter<BrawlerEnemyBattleResultStatisticsCollectionEntity> writer() {
        return new JpaItemListWriter<>(
                new JpaItemWriterBuilder<BrawlerEnemyBattleResultStatisticsCollectionEntity>()
                        .entityManagerFactory(emf)
                        .usePersist(false)
                        .build()
        );
    }
}

package com.imstargg.batch.job.statistics;

import com.imstargg.batch.domain.statistics.BattleItemReaderFactory;
import com.imstargg.batch.domain.statistics.BrawlerBattleResultStatisticsCollectorFactory;
import com.imstargg.batch.domain.statistics.StatisticsCheckPointer;
import com.imstargg.batch.job.support.ExceptionAlertJobExecutionListener;
import com.imstargg.batch.job.support.ItemListWriter;
import com.imstargg.batch.job.support.JdbcBatchItemInsertUpdateWriter;
import com.imstargg.storage.db.core.statistics.BrawlerBattleResultStatisticsCollectionEntity;
import com.imstargg.support.alert.AlertManager;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Configuration
public class BrawlerBattleResultStatisticsJobConfig {

    public static final String JOB_NAME = "brawlerBattleResultStatisticsJob";
    private static final String STEP_NAME = "brawlerBattleResultStatisticsStep";
    private static final int CHUNK_SIZE = 1;

    private final Clock clock;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;
    private final DataSource dataSource;

    private final AlertManager alertManager;
    private final StatisticsCheckPointer statisticsCheckPointer;
    private final BattleItemReaderFactory battleItemReaderFactory;
    private final BrawlerBattleResultStatisticsCollectorFactory collectorFactory;


    public BrawlerBattleResultStatisticsJobConfig(
            Clock clock,
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            DataSource dataSource,
            AlertManager alertManager,
            StatisticsCheckPointer statisticsCheckPointer,
            BattleItemReaderFactory battleItemReaderFactory,
            BrawlerBattleResultStatisticsCollectorFactory collectorFactory
    ) {
        this.clock = clock;
        this.jobRepository = jobRepository;
        this.txManager = txManager;
        this.dataSource = dataSource;
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
                .<LocalDate, List<BrawlerBattleResultStatisticsCollectionEntity>>chunk(CHUNK_SIZE, txManager)
                .reader(reader())
                .processor(processor())
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
    StatisticsJobItemProcessor<BrawlerBattleResultStatisticsCollectionEntity> processor() {
        return new StatisticsJobItemProcessor<>(
                JOB_NAME,
                statisticsCheckPointer,
                battleItemReaderFactory,
                collectorFactory
        );
    }

    @Bean(STEP_NAME + "ItemWriter")
    @StepScope
    ItemListWriter<BrawlerBattleResultStatisticsCollectionEntity> writer() {
        return new ItemListWriter<>(
                new JdbcBatchItemInsertUpdateWriter<>(
                        new JdbcBatchItemWriterBuilder<BrawlerBattleResultStatisticsCollectionEntity>()
                                .dataSource(dataSource)
                                .beanMapped()
                                .sql("""
                                        INSERT INTO brawler_battle_result_stats_v3
                                        (
                                            event_brawlstars_id,
                                            brawler_brawlstars_id,
                                            tier_range,
                                            battle_date,
                                            star_player_count,
                                            victory_count,
                                            defeat_count,
                                            draw_count,
                                            deleted
                                        )
                                        VALUES
                                        (
                                            :eventBrawlStarsId,
                                            :brawlerBrawlStarsId,
                                            :tierRange,
                                            :battleDate,
                                            :starPlayerCount,
                                            :victoryCount,
                                            :defeatCount,
                                            :drawCount,
                                            :deleted
                                        )
                                        """)
                                .build(),
                        new JdbcBatchItemWriterBuilder<BrawlerBattleResultStatisticsCollectionEntity>()
                                .dataSource(dataSource)
                                .beanMapped()
                                .sql("""
                                        UPDATE brawler_battle_result_stats_v3
                                        SET
                                            victory_count = :victoryCount,
                                            defeat_count = :defeatCount,
                                            draw_count = :drawCount,
                                            deleted = :deleted
                                        WHERE
                                            brawler_battle_result_stats_id = :id
                                        """)
                                .build(),
                        item -> item.getId() == null
                )
        );
    }
}
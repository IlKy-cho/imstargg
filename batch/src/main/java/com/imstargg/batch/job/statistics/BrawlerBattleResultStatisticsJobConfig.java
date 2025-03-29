package com.imstargg.batch.job.statistics;

import com.imstargg.batch.domain.statistics.BattleStatisticsCollectionValidator;
import com.imstargg.batch.domain.statistics.BrawlerBattleResultStatisticsCollector;
import com.imstargg.batch.domain.statistics.StatisticsCollector;
import com.imstargg.batch.job.BattleItemReaderFactory;
import com.imstargg.batch.job.support.ExceptionAlertJobExecutionListener;
import com.imstargg.batch.job.support.IdRangeIncrementer;
import com.imstargg.batch.job.support.IdRangeJobParameter;
import com.imstargg.batch.job.support.querydsl.QuerydslEntityCursorItemReader;
import com.imstargg.storage.db.core.player.BattleCollectionEntity;
import com.imstargg.storage.db.core.player.BattleCollectionJpaRepository;
import com.imstargg.storage.db.core.statistics.BrawlerBattleResultStatisticsCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerBattleResultStatisticsCollectionJpaRepository;
import com.imstargg.support.alert.AlertManager;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.Clock;
import java.util.Objects;

@Configuration
public class BrawlerBattleResultStatisticsJobConfig {

    private static final String JOB_NAME = "brawlerBattleResultStatisticsJob";
    private static final String STEP_NAME = "brawlerBattleResultStatisticsStep";
    private static final int CHUNK_SIZE = 1000;

    private final Clock clock;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;

    private final AlertManager alertManager;
    private final BattleCollectionJpaRepository battleCollectionJpaRepository;
    private final BrawlerBattleResultStatisticsCollectionJpaRepository brawlerBattleResultStatisticsCollectionJpaRepository;
    private final BattleItemReaderFactory battleItemReaderFactory;

    public BrawlerBattleResultStatisticsJobConfig(
            Clock clock,
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            AlertManager alertManager,
            BattleCollectionJpaRepository battleCollectionJpaRepository,
            BrawlerBattleResultStatisticsCollectionJpaRepository brawlerBattleResultStatisticsCollectionJpaRepository,
            BattleItemReaderFactory battleItemReaderFactory
    ) {
        this.clock = clock;
        this.jobRepository = jobRepository;
        this.txManager = txManager;
        this.alertManager = alertManager;
        this.battleCollectionJpaRepository = battleCollectionJpaRepository;
        this.brawlerBattleResultStatisticsCollectionJpaRepository = brawlerBattleResultStatisticsCollectionJpaRepository;
        this.battleItemReaderFactory = battleItemReaderFactory;
    }

    @Bean(JOB_NAME)
    Job job() {
        JobBuilder jobBuilder = new JobBuilder(JOB_NAME, jobRepository);
        return jobBuilder
                .start(step())
                .listener(new ExceptionAlertJobExecutionListener(alertManager))
                .validator(new DefaultJobParametersValidator(
                        new String[]{IdRangeJobParameter.ID_FROM_KEY, IdRangeJobParameter.ID_TO_KEY}, new String[]{})
                )
                .incrementer(new IdRangeIncrementer(() ->
                        battleCollectionJpaRepository.findFirst1ByOrderByIdDesc()
                                .map(BattleCollectionEntity::getId)
                                .orElse(0L)
                ))
                .build();
    }

    @Bean(JOB_NAME + "IdRangeJobParameter")
    @JobScope
    IdRangeJobParameter idRangeJobParameter() {
        return new IdRangeJobParameter();
    }

    @Bean(STEP_NAME)
    @JobScope
    Step step() {
        StepBuilder stepBuilder = new StepBuilder(STEP_NAME, jobRepository);
        return stepBuilder
                .<BattleCollectionEntity, BattleCollectionEntity>chunk(CHUNK_SIZE, txManager)
                .reader(reader())
                .writer(writer())
                .listener(new StepExecutionListener() {
                    @Override
                    public ExitStatus afterStep(StepExecution stepExecution) {
                        collector().save();
                        return null;
                    }
                })
                .build();
    }

    @Bean(STEP_NAME + "ItemReader" )
    @StepScope
    QuerydslEntityCursorItemReader<BattleCollectionEntity> reader() {
        return battleItemReaderFactory.create(
                CHUNK_SIZE,
                Objects.requireNonNull(idRangeJobParameter().getFrom()),
                Objects.requireNonNull(idRangeJobParameter().getTo())
        );
    }

    @Bean(STEP_NAME + "ItemWriter" )
    @StepScope
    StatisticsJobItemWriter<BrawlerBattleResultStatisticsCollectionEntity> writer() {
        return new StatisticsJobItemWriter<>(collector());
    }

    @Bean(STEP_NAME + "StatisticsCollector")
    @StepScope
    StatisticsCollector<BrawlerBattleResultStatisticsCollectionEntity> collector() {
        return new BrawlerBattleResultStatisticsCollector(
                battleStatisticsCollectionValidator(),
                brawlerBattleResultStatisticsCollectionJpaRepository
        );
    }

    @Bean(STEP_NAME + "BattleStatisticsCollectionValidator")
    @StepScope
    BattleStatisticsCollectionValidator battleStatisticsCollectionValidator() {
        return new BattleStatisticsCollectionValidator(clock);
    }
}

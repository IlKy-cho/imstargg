package com.imstargg.batch.job;

import com.imstargg.batch.domain.PlayerTagSet;
import com.imstargg.batch.job.support.ExceptionAlertJobExecutionListener;
import com.imstargg.batch.job.support.PeriodDateTimeJobParameter;
import com.imstargg.batch.job.support.QuerydslPagingItemReader;
import com.imstargg.storage.db.core.BattlePlayerCollectionEntity;
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

import static com.imstargg.storage.db.core.QBattlePlayerCollectionEntity.battlePlayerCollectionEntity;

@Configuration
class NewPlayerFindJobConfig {

    private static final String JOB_NAME = "newPlayerFindJob";
    private static final String STEP_NAME = "newPlayerFindStep";
    private static final int CHUNK_SIZE = 1000;

    private final Clock clock;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;
    private final EntityManagerFactory emf;

    private final AlertManager alertManager;

    NewPlayerFindJobConfig(
            Clock clock,
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            EntityManagerFactory emf,
            AlertManager alertManager
    ) {
        this.clock = clock;
        this.jobRepository = jobRepository;
        this.txManager = txManager;
        this.emf = emf;
        this.alertManager = alertManager;
    }

    @Bean(JOB_NAME)
    Job job() {
        JobBuilder jobBuilder = new JobBuilder(JOB_NAME, jobRepository);
        return jobBuilder
                .start(step())
                .listener(new ExceptionAlertJobExecutionListener(alertManager))
                .build();
    }

    @Bean(JOB_NAME + "PeriodDateTimeJobParameter")
    @JobScope
    PeriodDateTimeJobParameter periodDateTimeJobParameter() {
        return new PeriodDateTimeJobParameter();
    }


    @Bean(STEP_NAME)
    @JobScope
    Step step() {
        StepBuilder stepBuilder = new StepBuilder(STEP_NAME, jobRepository);
        return stepBuilder
                .<BattlePlayerCollectionEntity, UnknownPlayerCollectionEntity>chunk(CHUNK_SIZE, txManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean(STEP_NAME + "ItemReader")
    @StepScope
    QuerydslPagingItemReader<BattlePlayerCollectionEntity> reader() {
        return new QuerydslPagingItemReader<>(emf, CHUNK_SIZE, false, queryFactory -> queryFactory
                .selectFrom(battlePlayerCollectionEntity)
                .join(battlePlayerCollectionEntity.battle)
                .where(
                        battlePlayerCollectionEntity.battle.battleTime.goe(periodDateTimeJobParameter().getFrom()),
                        battlePlayerCollectionEntity.battle.battleTime.lt(periodDateTimeJobParameter().getTo())
                )
                .orderBy(battlePlayerCollectionEntity.battle.battleTime.desc())
        );
    }

    @Bean(STEP_NAME + "ItemProcessor")
    @StepScope
    NewPlayerFindJobItemProcessor processor() {
        return new NewPlayerFindJobItemProcessor(clock, playerTagSet());
    }

    @Bean(STEP_NAME + "ItemWriter")
    @StepScope
    NewPlayerFindJobItemWriter writer() {
        return new NewPlayerFindJobItemWriter(emf, playerTagSet());
    }

    @Bean(JOB_NAME + "PlayerTagSet")
    @JobScope
    PlayerTagSet playerTagSet() {
        return new PlayerTagSet(emf);
    }
}

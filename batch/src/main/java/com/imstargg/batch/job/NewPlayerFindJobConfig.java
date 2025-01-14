package com.imstargg.batch.job;

import com.imstargg.batch.job.support.DateJobParameter;
import com.imstargg.batch.job.support.ExceptionAlertJobExecutionListener;
import com.imstargg.batch.job.support.querydsl.QuerydslPagingItemReader;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.UnknownPlayerCollectionEntity;
import com.imstargg.support.alert.AlertManager;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.Clock;
import java.util.List;
import java.util.Objects;

import static com.imstargg.storage.db.core.QBattleCollectionEntity.battleCollectionEntity;

@Configuration
class NewPlayerFindJobConfig {

    private static final Logger log = LoggerFactory.getLogger(NewPlayerFindJobConfig.class);

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
                .validator(new DefaultJobParametersValidator(new String[]{"date"}, new String[]{}))
                .build();
    }

    @Bean(JOB_NAME + "DateJobParameter")
    @JobScope
    DateJobParameter dateJobParameter() {
        return new DateJobParameter();
    }


    @Bean(STEP_NAME)
    @JobScope
    Step step() {
        StepBuilder stepBuilder = new StepBuilder(STEP_NAME, jobRepository);
        return stepBuilder
                .<BattleCollectionEntity, List<UnknownPlayerCollectionEntity>>chunk(CHUNK_SIZE, txManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())

                .faultTolerant()
                .retryLimit(3)
                .retry(SQLIntegrityConstraintViolationException.class)
                .listener(new ItemWriteListener<>() {
                    @Override
                    public void onWriteError(
                            Exception exception, Chunk<? extends List<UnknownPlayerCollectionEntity>> items) {
                        log.warn("{} 중 중복된 플레이어 저장 발생. items={}",
                                JOB_NAME,
                                items.getItems().stream()
                                        .flatMap(List::stream)
                                        .map(UnknownPlayerCollectionEntity::getBrawlStarsTag).toList(),
                                exception
                        );
                    }
                })

                .build();
    }

    @Bean(STEP_NAME + "ItemReader")
    @StepScope
    QuerydslPagingItemReader<BattleCollectionEntity> reader() {
        return new QuerydslPagingItemReader<>(emf, CHUNK_SIZE, false, queryFactory -> queryFactory
                .selectFrom(battleCollectionEntity)
                .where(
                        battleCollectionEntity.battleTime.goe(
                                Objects.requireNonNull(dateJobParameter().getDate()).atStartOfDay().atZone(clock.getZone()).toOffsetDateTime()
                        ),
                        battleCollectionEntity.battleTime.lt(
                                Objects.requireNonNull(dateJobParameter().getDate()).plusDays(1).atStartOfDay().atZone(clock.getZone()).toOffsetDateTime()
                        )
                )
                .orderBy(battleCollectionEntity.id.desc())
        );
    }

    @Bean(STEP_NAME + "ItemProcessor")
    @StepScope
    NewPlayerFindJobItemProcessor processor() {
        return new NewPlayerFindJobItemProcessor();
    }

    @Bean(STEP_NAME + "ItemWriter")
    @StepScope
    NewPlayerFindJobItemWriter writer() {
        return new NewPlayerFindJobItemWriter(emf);
    }
}

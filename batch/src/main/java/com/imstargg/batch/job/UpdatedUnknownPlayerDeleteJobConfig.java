package com.imstargg.batch.job;

import com.imstargg.batch.job.support.ExceptionAlertJobExecutionListener;
import com.imstargg.batch.job.support.PeriodDateTimeJobParameter;
import com.imstargg.batch.job.support.querydsl.QuerydslZeroPagingItemReader;
import com.imstargg.core.enums.UnknownPlayerStatus;
import com.imstargg.storage.db.core.UnknownPlayerCollectionEntity;
import com.imstargg.storage.db.core.UnknownPlayerCollectionJpaRepository;
import com.imstargg.support.alert.AlertManager;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import static com.imstargg.storage.db.core.QUnknownPlayerCollectionEntity.unknownPlayerCollectionEntity;

@Configuration
class UpdatedUnknownPlayerDeleteJobConfig {

    private static final Logger log = LoggerFactory.getLogger(UpdatedUnknownPlayerDeleteJobConfig.class);

    private static final String JOB_NAME = "updatedUnknownPlayerDeleteJob";
    private static final String STEP_NAME = "updatedUnknownPlayerDeleteStep";
    private static final int CHUNK_SIZE = 100;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;
    private final EntityManagerFactory emf;

    private final AlertManager alertManager;
    private final UnknownPlayerCollectionJpaRepository unknownPlayerJpaRepository;

    UpdatedUnknownPlayerDeleteJobConfig(
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            EntityManagerFactory emf,
            AlertManager alertManager,
            UnknownPlayerCollectionJpaRepository unknownPlayerJpaRepository
    ) {
        this.jobRepository = jobRepository;
        this.txManager = txManager;
        this.emf = emf;
        this.alertManager = alertManager;
        this.unknownPlayerJpaRepository = unknownPlayerJpaRepository;
    }

    @Bean(JOB_NAME)
    Job job() {
        JobBuilder jobBuilder = new JobBuilder(JOB_NAME, jobRepository);
        return jobBuilder
                .start(step())
                .incrementer(new RunIdIncrementer())
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
                .<UnknownPlayerCollectionEntity, UnknownPlayerCollectionEntity>chunk(CHUNK_SIZE, txManager)
                .reader(reader())
                .writer(writer())
                .build();
    }

    @Bean(STEP_NAME + "ItemReader")
    @StepScope
    QuerydslZeroPagingItemReader<UnknownPlayerCollectionEntity> reader() {
        var reader = new QuerydslZeroPagingItemReader<>(emf, CHUNK_SIZE, queryFactory -> queryFactory
                .selectFrom(unknownPlayerCollectionEntity)
                .where(
                        unknownPlayerCollectionEntity.status.eq(UnknownPlayerStatus.UPDATED)
                )
        );
        reader.setTransacted(false);
        reader.setSaveState(false);
        return reader;
    }

    @Bean(STEP_NAME + "ItemWriter")
    @StepScope
    ItemWriter<UnknownPlayerCollectionEntity> writer() {
        return items -> {
            unknownPlayerJpaRepository.deleteAllByIdInBatch(
                    items.getItems().stream().map(UnknownPlayerCollectionEntity::getId).toList()
            );
        };
    }
}

package com.imstargg.batch.job;

import com.imstargg.batch.job.support.DateJobParameter;
import com.imstargg.batch.job.support.ExceptionAlertJobExecutionListener;
import com.imstargg.batch.job.support.querydsl.QuerydslPagingItemReader;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.collection.UnregisteredBattleEventCollectionEntity;
import com.imstargg.support.alert.AlertManager;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Objects;
import java.util.concurrent.ConcurrentSkipListSet;

import static com.imstargg.storage.db.core.QBattleCollectionEntity.battleCollectionEntity;
import static com.imstargg.storage.db.core.brawlstars.QBattleEventCollectionEntity.battleEventCollectionEntity;
import static com.imstargg.storage.db.core.collection.QUnregisteredBattleEventCollectionEntity.unregisteredBattleEventCollectionEntity;

@Configuration
public class UnregisteredBattleEventFindJobConfig {

    private static final String JOB_NAME = "unregisteredBattleEventFindJob";
    private static final String STEP_NAME = "unregisteredBattleEventFindStep";
    private static final int CHUNK_SIZE = 1000;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;
    private final EntityManagerFactory emf;
    private final AlertManager alertManager;

    UnregisteredBattleEventFindJobConfig(
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            EntityManagerFactory emf,
            AlertManager alertManager
    ) {
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
                .<BattleCollectionEntity, UnregisteredBattleEventCollectionEntity>chunk(CHUNK_SIZE, txManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean(STEP_NAME + "ItemReader")
    @StepScope
    QuerydslPagingItemReader<BattleCollectionEntity> reader() {
        return new QuerydslPagingItemReader<>(emf, CHUNK_SIZE, false, queryFactory -> queryFactory
                .selectFrom(battleCollectionEntity)
                .where(
                        battleCollectionEntity.createdAt.goe(
                                Objects.requireNonNull(dateJobParameter().getDate()).atStartOfDay()
                        ),
                        battleCollectionEntity.createdAt.lt(
                                Objects.requireNonNull(dateJobParameter().getDate()).plusDays(1).atStartOfDay()
                        )
                )
                .orderBy(battleCollectionEntity.createdAt.asc())
        );
    }

    @Bean(STEP_NAME + "ItemProcessor")
    @StepScope
    ItemProcessor<BattleCollectionEntity, UnregisteredBattleEventCollectionEntity> processor() {
        var queryFactory = new JPAQueryFactory(EntityManagerFactoryUtils.getTransactionalEntityManager(emf));
        var eventBrawlStarsIdSet = new ConcurrentSkipListSet<Long>();
        eventBrawlStarsIdSet.addAll(
                queryFactory.select(battleEventCollectionEntity.brawlStarsId)
                        .from(battleEventCollectionEntity)
                        .fetch()
        );
        eventBrawlStarsIdSet.addAll(
                queryFactory.select(unregisteredBattleEventCollectionEntity.battle.event.brawlStarsId)
                        .from(unregisteredBattleEventCollectionEntity)
                        .fetch()
        );

        return battle -> {
            if (eventBrawlStarsIdSet.contains(battle.getEvent().getBrawlStarsId())) {
                return null;
            }

            eventBrawlStarsIdSet.add(battle.getEvent().getBrawlStarsId());
            return new UnregisteredBattleEventCollectionEntity(battle);
        };
    }

    @Bean(STEP_NAME + "ItemWriter")
    @StepScope
    JpaItemWriter<UnregisteredBattleEventCollectionEntity> writer() {
        return new JpaItemWriterBuilder<UnregisteredBattleEventCollectionEntity>()
                .entityManagerFactory(emf)
                .usePersist(true)
                .build();
    }
}

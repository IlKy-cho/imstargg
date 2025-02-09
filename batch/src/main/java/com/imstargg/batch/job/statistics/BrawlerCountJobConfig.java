package com.imstargg.batch.job.statistics;

import com.imstargg.batch.job.support.ExceptionAlertJobExecutionListener;
import com.imstargg.storage.db.core.PlayerBrawlerCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerCountCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerItemCountCollectionEntity;
import com.imstargg.support.alert.AlertManager;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.builder.TaskletStepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.imstargg.storage.db.core.QPlayerBrawlerCollectionEntity.playerBrawlerCollectionEntity;
import static com.imstargg.storage.db.core.statistics.QBrawlerCountCollectionEntity.brawlerCountCollectionEntity;
import static com.imstargg.storage.db.core.statistics.QBrawlerItemCountCollectionEntity.brawlerItemCountCollectionEntity;

@Configuration
public class BrawlerCountJobConfig {

    private static final Logger log = LoggerFactory.getLogger(BrawlerCountJobConfig.class);

    private static final String JOB_NAME = "brawlerCountJob";
    private static final String STEP_NAME = "brawlerCountStep";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;
    private final EntityManagerFactory emf;

    private final AlertManager alertManager;

    public BrawlerCountJobConfig(
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
                .incrementer(new RunIdIncrementer())
                .listener(new ExceptionAlertJobExecutionListener(alertManager))
                .build();
    }

    @Bean(STEP_NAME)
    @JobScope
    public Step step() {
        TaskletStepBuilder taskletStepBuilder = new TaskletStepBuilder(new StepBuilder(STEP_NAME, jobRepository));
        return taskletStepBuilder
                .tasklet((contribution, chunkContext) -> {
                    EntityManager em = EntityManagerFactoryUtils.getTransactionalEntityManager(emf);
                    if (em == null) {
                        throw new IllegalStateException("No transactional EntityManager found");
                    }
                    JPAQueryFactory queryFactory = new JPAQueryFactory(em);

                    Long cursorPlayerBrawlerId = null;
                    int size = 10_000;
                    boolean hasMore = true;
                    Map<BrawlerItemKey, Integer> brawlerItemCounts = new HashMap<>();
                    Map<Long, Integer> brawlerCounts = new HashMap<>();
                    while (hasMore) {
                        log.debug("processing cursorPlayerBrawlerId={}", cursorPlayerBrawlerId);
                        List<PlayerBrawlerCollectionEntity> playerBrawlerEntities = queryFactory
                                .selectFrom(playerBrawlerCollectionEntity)
                                .where(
                                        cursorPlayerBrawlerId == null ? null : playerBrawlerCollectionEntity.id.gt(cursorPlayerBrawlerId)
                                ).orderBy(playerBrawlerCollectionEntity.id.asc())
                                .limit(size)
                                .fetch();
                        playerBrawlerEntities.forEach(em::detach);
                        em.clear();
                        if (playerBrawlerEntities.isEmpty()) {
                            break;
                        }
                        hasMore = playerBrawlerEntities.size() == size;
                        cursorPlayerBrawlerId = playerBrawlerEntities.getLast().getId();

                        playerBrawlerEntities.forEach(playerBrawlerEntity -> {
                            long brawlerBrawlStarsId = playerBrawlerEntity.getBrawlerBrawlStarsId();
                            brawlerCounts.compute(brawlerBrawlStarsId, (k, v) -> v == null ? 1 : v + 1);
                            playerBrawlerEntity.getGadgetBrawlStarsIds().forEach(gadgetBrawlStarsId ->
                                    brawlerItemCounts.compute(
                                            new BrawlerItemKey(brawlerBrawlStarsId, gadgetBrawlStarsId),
                                            (k, v) -> v == null ? 1 : v + 1
                                    ));
                            playerBrawlerEntity.getStarPowerBrawlStarsIds().forEach(starPowerBrawlStarsId ->
                                    brawlerItemCounts.compute(
                                            new BrawlerItemKey(brawlerBrawlStarsId, starPowerBrawlStarsId),
                                            (k, v) -> v == null ? 1 : v + 1
                                    ));
                            playerBrawlerEntity.getGearBrawlStarsIds().forEach(gearBrawlStarsId ->
                                    brawlerItemCounts.compute(
                                            new BrawlerItemKey(brawlerBrawlStarsId, gearBrawlStarsId),
                                            (k, v) -> v == null ? 1 : v + 1
                                    ));
                        });
                    }

                    brawlerCounts.forEach((brawlerBrawlStarsId, count) ->
                            Optional.ofNullable(
                                    queryFactory.selectFrom(brawlerCountCollectionEntity)
                                            .where(brawlerCountCollectionEntity.brawlerBrawlStarsId.eq(brawlerBrawlStarsId))
                                            .fetchOne()
                            ).orElseGet(() -> {
                                var entity = new BrawlerCountCollectionEntity(brawlerBrawlStarsId, count);
                                em.persist(entity);
                                return entity;
                            }).update(count));

                    brawlerItemCounts.forEach((brawlerItemKey, count) ->
                            Optional.ofNullable(
                                    queryFactory.selectFrom(brawlerItemCountCollectionEntity)
                                            .where(
                                                    brawlerItemCountCollectionEntity.brawlerBrawlStarsId
                                                            .eq(brawlerItemKey.brawlerBrawlStarsId),
                                                    brawlerItemCountCollectionEntity.itemBrawlStarsId
                                                            .eq(brawlerItemKey.itemBrawlStarsId)
                                            ).fetchOne()
                            ).orElseGet(() -> {
                                var entity = new BrawlerItemCountCollectionEntity(
                                        brawlerItemKey.brawlerBrawlStarsId,
                                        brawlerItemKey.itemBrawlStarsId,
                                        count
                                );
                                em.persist(entity);
                                return entity;
                            }).update(count));

                    return RepeatStatus.FINISHED;
                }, txManager)
                .build();
    }

    private record BrawlerItemKey(long brawlerBrawlStarsId, long itemBrawlStarsId) {
    }

}

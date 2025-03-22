package com.imstargg.batch.job.statistics;

import com.imstargg.batch.job.support.ExceptionAlertJobExecutionListener;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.player.PlayerBrawlerCollectionEntity;
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
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.imstargg.storage.db.core.player.QPlayerBrawlerCollectionEntity.playerBrawlerCollectionEntity;
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

                    Long cursorPlayerBrawlerId = null;
                    int size = 10_000;
                    boolean hasMore = true;
                    Map<BrawlerItemCountKey, Integer> brawlerItemCounts = new HashMap<>();
                    Map<BrawlerCountKey, Integer> brawlerCounts = new HashMap<>();
                    while (hasMore) {
                        log.debug("processing cursorPlayerBrawlerId={}", cursorPlayerBrawlerId);
                        List<PlayerBrawlerCollectionEntity> playerBrawlerEntities = fetch(em, cursorPlayerBrawlerId, size);
                        playerBrawlerEntities.forEach(em::detach);
                        em.clear();
                        if (playerBrawlerEntities.isEmpty()) {
                            break;
                        }
                        hasMore = playerBrawlerEntities.size() == size;
                        cursorPlayerBrawlerId = playerBrawlerEntities.getLast().getId();

                        playerBrawlerEntities.forEach(playerBrawlerEntity -> {
                            long brawlerBrawlStarsId = playerBrawlerEntity.getBrawlerBrawlStarsId();
                            TrophyRange trophyRange = TrophyRange.of(playerBrawlerEntity.getHighestTrophies());
                            brawlerCounts.compute(
                                    new BrawlerCountKey(brawlerBrawlStarsId, trophyRange),
                                    (k, v) -> v == null ? 1 : v + 1
                            );
                            playerBrawlerEntity.getGadgetBrawlStarsIds().forEach(gadgetBrawlStarsId ->
                                    brawlerItemCounts.compute(
                                            new BrawlerItemCountKey(brawlerBrawlStarsId, gadgetBrawlStarsId, trophyRange),
                                            (k, v) -> v == null ? 1 : v + 1
                                    ));
                            playerBrawlerEntity.getStarPowerBrawlStarsIds().forEach(starPowerBrawlStarsId ->
                                    brawlerItemCounts.compute(
                                            new BrawlerItemCountKey(brawlerBrawlStarsId, starPowerBrawlStarsId, trophyRange),
                                            (k, v) -> v == null ? 1 : v + 1
                                    ));
                            playerBrawlerEntity.getGearBrawlStarsIds().forEach(gearBrawlStarsId ->
                                    brawlerItemCounts.compute(
                                            new BrawlerItemCountKey(brawlerBrawlStarsId, gearBrawlStarsId, trophyRange),
                                            (k, v) -> v == null ? 1 : v + 1
                                    ));
                        });
                    }

                    saveBrawlerCounts(brawlerCounts, em);

                    saveBrawlerItemCounts(brawlerItemCounts, em);

                    return RepeatStatus.FINISHED;
                }, txManager)
                .build();
    }

    private List<PlayerBrawlerCollectionEntity> fetch(EntityManager em, Long cursorPlayerBrawlerId, int size) {
        var queryFactory = new JPAQueryFactory(em);
        return queryFactory
                .selectFrom(playerBrawlerCollectionEntity)
                .where(
                        cursorPlayerBrawlerId == null ?
                                null : playerBrawlerCollectionEntity.id.gt(cursorPlayerBrawlerId)
                ).orderBy(playerBrawlerCollectionEntity.id.asc())
                .limit(size)
                .fetch();
    }

    private void saveBrawlerCounts(Map<BrawlerCountKey, Integer> brawlerCounts, EntityManager em) {
        var queryFactory = new JPAQueryFactory(em);
        var brawlerCountEntities = new HashMap<>(queryFactory
                .selectFrom(brawlerCountCollectionEntity)
                .fetch()
                .stream()
                .collect(Collectors.toMap(
                        brawlerCountCollectionEntity -> new BrawlerCountKey(
                                brawlerCountCollectionEntity.getBrawlerBrawlStarsId(),
                                brawlerCountCollectionEntity.getTrophyRange()
                        ),
                        Function.identity()
                )));
        brawlerCounts.forEach((key, count) ->
                Optional.ofNullable(brawlerCountEntities.get(key)).ifPresentOrElse(
                        entity -> entity.update(count),
                        () -> {
                            var entity = new BrawlerCountCollectionEntity(
                                    key.brawlerBrawlStarsId(), key.trophyRange(), count
                            );
                            em.persist(entity);
                            brawlerCountEntities.put(key, entity);
                        }
                ));
    }

    private void saveBrawlerItemCounts(Map<BrawlerItemCountKey, Integer> brawlerItemCounts, EntityManager em) {
        var queryFactory = new JPAQueryFactory(em);
        var brawlerItemCountEntities = new HashMap<>(queryFactory
                .selectFrom(brawlerItemCountCollectionEntity)
                .fetch()
                .stream()
                .collect(Collectors.toMap(
                        brawlerItemCountCollectionEntity -> new BrawlerItemCountKey(
                                brawlerItemCountCollectionEntity.getBrawlerBrawlStarsId(),
                                brawlerItemCountCollectionEntity.getItemBrawlStarsId(),
                                brawlerItemCountCollectionEntity.getTrophyRange()
                        ),
                        Function.identity()
                )));

        brawlerItemCounts.forEach((key, count) ->
                Optional.ofNullable(brawlerItemCountEntities.get(key)).ifPresentOrElse(
                        entity -> entity.update(count),
                        () -> {
                            var entity = new BrawlerItemCountCollectionEntity(
                                    key.brawlerBrawlStarsId(), key.itemBrawlStarsId(), key.trophyRange(), count
                            );
                            em.persist(entity);
                            brawlerItemCountEntities.put(key, entity);
                        }
                ));
    }

    private record BrawlerCountKey(long brawlerBrawlStarsId, TrophyRange trophyRange) {
    }

    private record BrawlerItemCountKey(long brawlerBrawlStarsId, long itemBrawlStarsId, TrophyRange trophyRange) {
    }

}

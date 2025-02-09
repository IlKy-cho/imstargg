package com.imstargg.batch.job.statistics;

import com.imstargg.batch.job.support.ExceptionAlertJobExecutionListener;
import com.imstargg.storage.db.core.PlayerBrawlerCollectionEntity;
import com.imstargg.storage.db.core.cache.BrawlerCountCacheKey;
import com.imstargg.storage.db.core.cache.CacheKey;
import com.imstargg.storage.db.core.cache.GadgetCountCacheKey;
import com.imstargg.storage.db.core.cache.GearCountCacheKey;
import com.imstargg.storage.db.core.cache.PlayerCountCacheKey;
import com.imstargg.storage.db.core.cache.RdsCacheCollectionEntity;
import com.imstargg.storage.db.core.cache.RdsCacheCollectionJpaRepository;
import com.imstargg.storage.db.core.cache.StarPowerCountCacheKey;
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
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.imstargg.storage.db.core.QPlayerBrawlerCollectionEntity.playerBrawlerCollectionEntity;
import static com.imstargg.storage.db.core.QPlayerCollectionEntity.playerCollectionEntity;

@Configuration
public class PlayerBrawlerCountJobConfig {

    private static final Logger log = LoggerFactory.getLogger(PlayerBrawlerCountJobConfig.class);

    private static final String JOB_NAME = "playerBrawlerCountJob";
    private static final String STEP_NAME = "playerBrawlerCountStep";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;
    private final EntityManagerFactory emf;

    private final AlertManager alertManager;
    private final RdsCacheCollectionJpaRepository rdsCacheCollectionJpaRepository;

    public PlayerBrawlerCountJobConfig(
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            EntityManagerFactory emf,
            AlertManager alertManager,
            RdsCacheCollectionJpaRepository rdsCacheCollectionJpaRepository
    ) {
        this.jobRepository = jobRepository;
        this.txManager = txManager;
        this.emf = emf;
        this.alertManager = alertManager;
        this.rdsCacheCollectionJpaRepository = rdsCacheCollectionJpaRepository;
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
                    long playerCount = 0;
                    BrawlerCountCollector collector = new BrawlerCountCollector();

                    Long cursorPlayerId = null;
                    int size = 500;
                    boolean hasMore = true;
                    while (hasMore) {
                        log.debug("processing cursorPlayerId={}", cursorPlayerId);
                        List<Long> playerIds = queryFactory
                                .select(playerCollectionEntity.id)
                                .from(playerCollectionEntity)
                                .where(
                                        cursorPlayerId == null ? null : playerCollectionEntity.id.gt(cursorPlayerId)
                                ).orderBy(playerCollectionEntity.id.asc())
                                .limit(size)
                                .fetch();
                        if (playerIds.isEmpty()) {
                            break;
                        }
                        hasMore = playerIds.size() == size;
                        cursorPlayerId = playerIds.getLast();

                        List<PlayerBrawlerCollectionEntity> playerBrawlerEntities = queryFactory
                                .selectFrom(playerBrawlerCollectionEntity)
                                .where(playerBrawlerCollectionEntity.player.id.in(playerIds))
                                .fetch();
                        playerBrawlerEntities.forEach(em::detach);
                        em.clear();

                        playerCount += playerIds.size();
                        collector.collectEntities(playerBrawlerEntities);
                    }

                    collector.save(rdsCacheCollectionJpaRepository);
                    var playerCountEntity = rdsCacheCollectionJpaRepository
                            .findByKey(PlayerCountCacheKey.KEY)
                            .orElse(new RdsCacheCollectionEntity(PlayerCountCacheKey.KEY, String.valueOf(playerCount)));
                    playerCountEntity.update(String.valueOf(playerCount));
                    rdsCacheCollectionJpaRepository.save(playerCountEntity);
                    return RepeatStatus.FINISHED;
                }, txManager)
                .build();
    }

    private static class BrawlerCountCollector {

        private final Map<BrawlerCountCacheKey, Long> brawlerCounts = new HashMap<>();
        private final Map<GadgetCountCacheKey, Long> gadgetCounts = new HashMap<>();
        private final Map<StarPowerCountCacheKey, Long> starPowerCounts = new HashMap<>();
        private final Map<GearCountCacheKey, Long> gearCounts = new HashMap<>();

        public void collectEntities(List<PlayerBrawlerCollectionEntity> entities) {
            entities.forEach(this::collectEntity);
        }

        private void collectEntity(PlayerBrawlerCollectionEntity entity) {
            updateBrawlerCount(entity);
            updateGadgetCounts(entity);
            updateStarPowerCounts(entity);
            updateGearCounts(entity);
        }

        private void updateBrawlerCount(PlayerBrawlerCollectionEntity entity) {
            var key = new BrawlerCountCacheKey(entity.getBrawlerBrawlStarsId());
            brawlerCounts.merge(key, 1L, Long::sum);
        }

        private void updateGadgetCounts(PlayerBrawlerCollectionEntity entity) {
            entity.getGadgetBrawlStarsIds().forEach(id -> {
                var key = new GadgetCountCacheKey(id);
                gadgetCounts.merge(key, 1L, Long::sum);
            });
        }

        private void updateStarPowerCounts(PlayerBrawlerCollectionEntity entity) {
            entity.getStarPowerBrawlStarsIds().forEach(id -> {
                var key = new StarPowerCountCacheKey(id);
                starPowerCounts.merge(key, 1L, Long::sum);
            });
        }

        private void updateGearCounts(PlayerBrawlerCollectionEntity entity) {
            entity.getGearBrawlStarsIds().forEach(gearId -> {
                var key = new GearCountCacheKey(entity.getBrawlerBrawlStarsId(), gearId);
                gearCounts.merge(key, 1L, Long::sum);
            });
        }

        public void save(RdsCacheCollectionJpaRepository repository) {
            doSave(brawlerCounts, repository);
            doSave(gadgetCounts, repository);
            doSave(starPowerCounts, repository);
            doSave(gearCounts, repository);
        }

        private void doSave(Map<? extends CacheKey, Long> counts, RdsCacheCollectionJpaRepository repository) {
            var keyToEntity = new HashMap<>(
                    repository.findAllByKeyIn(counts.keySet().stream().map(CacheKey::key).toList())
                            .stream()
                            .collect(Collectors.toMap(RdsCacheCollectionEntity::getKey, Function.identity()))
            );
            counts.forEach((key, count) -> keyToEntity
                    .computeIfAbsent(key.key(), k -> new RdsCacheCollectionEntity(k, String.valueOf(count)))
                    .update(String.valueOf(count)));
            repository.saveAll(keyToEntity.values());
        }
    }

}

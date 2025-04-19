package com.imstargg.batch.domain.statistics;

import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.player.PlayerBrawlerCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerCountCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerItemCountCollectionEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.imstargg.storage.db.core.statistics.QBrawlerCountCollectionEntity.brawlerCountCollectionEntity;
import static com.imstargg.storage.db.core.statistics.QBrawlerItemCountCollectionEntity.brawlerItemCountCollectionEntity;

public class PlayerBrawlerStatisticsCollector {

    private static final Logger log = LoggerFactory.getLogger(PlayerBrawlerStatisticsCollector.class);

    private final EntityManagerFactory emf;
    private final Map<BrawlerCountKey, BrawlerCountCollectionEntity> brawlerCounts = new ConcurrentHashMap<>();
    private final Map<BrawlerItemCountKey, BrawlerItemCountCollectionEntity> brawlerItemCounts = new ConcurrentHashMap<>();

    public PlayerBrawlerStatisticsCollector(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @PostConstruct
    void init() {
        try (EntityManager em = emf.createEntityManager()) {
            JPAQueryFactory queryFactory = new JPAQueryFactory(em);
            List<BrawlerCountCollectionEntity> brawlerCountEntities = queryFactory
                    .selectFrom(brawlerCountCollectionEntity)
                    .fetch();
            brawlerCountEntities.forEach(em::detach);
            brawlerCountEntities.forEach(BrawlerCountCollectionEntity::init);
            brawlerCountEntities.forEach(item -> brawlerCounts.put(new BrawlerCountKey(
                    item.getBrawlerBrawlStarsId(), item.getTrophyRange()), item
            ));

            log.debug("총 {} 개의 기존 {}를 불러왔습니다.", brawlerCountEntities.size(), BrawlerCountCollectionEntity.class.getSimpleName());

            List<BrawlerItemCountCollectionEntity> brawlerItemCountEntities = queryFactory
                    .selectFrom(brawlerItemCountCollectionEntity)
                    .fetch();
            brawlerItemCountEntities.forEach(em::detach);
            brawlerItemCountEntities.forEach(BrawlerItemCountCollectionEntity::init);
            brawlerItemCountEntities.forEach(item -> brawlerItemCounts.put(new BrawlerItemCountKey(
                    item.getBrawlerBrawlStarsId(), item.getItemBrawlStarsId(), item.getTrophyRange()), item
            ));

            log.debug("총 {} 개의 기존 {}를 불러왔습니다.", brawlerItemCountEntities.size(), BrawlerItemCountCollectionEntity.class.getSimpleName());
        }
    }

    public void collect(PlayerBrawlerCollectionEntity playerBrawler) {
        TrophyRange.findAll(playerBrawler.getTrophies()).forEach(trophyRange -> {
            getBrawlerCount(playerBrawler.getBrawlerBrawlStarsId(), trophyRange).countUp();
            playerBrawler.getGadgetBrawlStarsIds().forEach(gadgetBrawlStarsId ->
                    getBrawlerItemCount(playerBrawler.getBrawlerBrawlStarsId(), gadgetBrawlStarsId, trophyRange).countUp());
            playerBrawler.getStarPowerBrawlStarsIds().forEach(starPowerBrawlStarsId ->
                    getBrawlerItemCount(playerBrawler.getBrawlerBrawlStarsId(), starPowerBrawlStarsId, trophyRange).countUp());
            playerBrawler.getGearBrawlStarsIds().forEach(gearBrawlStarsId ->
                    getBrawlerItemCount(playerBrawler.getBrawlerBrawlStarsId(), gearBrawlStarsId, trophyRange).countUp());
        });
    }

    private BrawlerCountCollectionEntity getBrawlerCount(long brawlerBrawlStarsId, TrophyRange trophyRange) {
        return brawlerCounts.computeIfAbsent(new BrawlerCountKey(brawlerBrawlStarsId, trophyRange), key ->
                new BrawlerCountCollectionEntity(key.brawlerBrawlStarsId, key.trophyRange, 0));
    }

    private BrawlerItemCountCollectionEntity getBrawlerItemCount(long brawlerBrawlStarsId, long itemBrawlStarsId, TrophyRange trophyRange) {
        return brawlerItemCounts.computeIfAbsent(new BrawlerItemCountKey(brawlerBrawlStarsId, itemBrawlStarsId, trophyRange), key ->
                new BrawlerItemCountCollectionEntity(key.brawlerBrawlStarsId, key.itemBrawlStarsId, key.trophyRange, 0));
    }

    @Transactional
    public void save() {
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            for (BrawlerCountCollectionEntity entity : brawlerCounts.values()) {
                em.merge(entity);
            }
            for (BrawlerItemCountCollectionEntity entity : brawlerItemCounts.values()) {
                em.merge(entity);
            }
            tx.commit();
        }
    }

    private record BrawlerCountKey(long brawlerBrawlStarsId, TrophyRange trophyRange) {
    }

    private record BrawlerItemCountKey(long brawlerBrawlStarsId, long itemBrawlStarsId, TrophyRange trophyRange) {
    }
}

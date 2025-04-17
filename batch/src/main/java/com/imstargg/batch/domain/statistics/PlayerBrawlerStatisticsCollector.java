package com.imstargg.batch.domain.statistics;

import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.player.PlayerBrawlerCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerCountCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerItemCountCollectionEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.imstargg.storage.db.core.statistics.QBrawlerCountCollectionEntity.brawlerCountCollectionEntity;
import static com.imstargg.storage.db.core.statistics.QBrawlerItemCountCollectionEntity.brawlerItemCountCollectionEntity;

public class PlayerBrawlerStatisticsCollector {

    private final EntityManager em;
    private final Map<BrawlerCountKey, BrawlerCountCollectionEntity> brawlerCounts = new ConcurrentHashMap<>();
    private final Map<BrawlerItemCountKey, BrawlerItemCountCollectionEntity> brawlerItemCounts = new ConcurrentHashMap<>();

    public PlayerBrawlerStatisticsCollector(EntityManager em) {
        this.em = em;
    }

    @PostConstruct
    void init() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        queryFactory
                .selectFrom(brawlerCountCollectionEntity)
                .fetch()
                .forEach(item -> brawlerCounts.put(new BrawlerCountKey(
                        item.getBrawlerBrawlStarsId(), item.getTrophyRange()), item)
                );
        brawlerCounts.values().forEach(BrawlerCountCollectionEntity::init);
        queryFactory
                .selectFrom(brawlerItemCountCollectionEntity)
                .fetch()
                .forEach(item -> brawlerItemCounts.put(new BrawlerItemCountKey(
                        item.getBrawlerBrawlStarsId(), item.getItemBrawlStarsId(), item.getTrophyRange()), item)
                );
        brawlerItemCounts.values().forEach(BrawlerItemCountCollectionEntity::init);
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
        em.persist(brawlerCounts.values());
        em.persist(brawlerItemCounts.values());
    }

    private record BrawlerCountKey(long brawlerBrawlStarsId, TrophyRange trophyRange) {
    }

    private record BrawlerItemCountKey(long brawlerBrawlStarsId, long itemBrawlStarsId, TrophyRange trophyRange) {
    }
}

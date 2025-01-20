package com.imstargg.batch.domain.statistics;

import com.imstargg.batch.util.JPAQueryFactoryUtils;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerBattleRankStatisticsCollectionEntity;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static com.imstargg.storage.db.core.statistics.QBrawlerBattleRankStatisticsCollectionEntity.brawlerBattleRankStatisticsCollectionEntity;


public class BrawlerBattleRankStatisticsCollector {

    private final ConcurrentHashMap<BrawlerBattleRankStatisticsKey, BrawlerBattleRankStatisticsCollectionEntity> cache
            = new ConcurrentHashMap<>();

    public BrawlerBattleRankStatisticsCollector(
            EntityManagerFactory emf, LocalDate battleDate, long eventBrawlStarsId
    ) {
        JPAQueryFactoryUtils.getQueryFactory(emf)
                .selectFrom(brawlerBattleRankStatisticsCollectionEntity)
                .where(
                        brawlerBattleRankStatisticsCollectionEntity.eventBrawlStarsId.eq(eventBrawlStarsId),
                        brawlerBattleRankStatisticsCollectionEntity.battleDate.eq(battleDate)
                ).fetch()
                .forEach(entity -> cache.put(
                        BrawlerBattleRankStatisticsKey.of(entity),
                        entity
                ));
    }

    public void collect(BattleCollectionEntity battle) {
        battle.playerCombinations().forEach(playerCombination -> {
            var key = BrawlerBattleRankStatisticsKey.of(battle);
            var brawlerBattleResultStats = getBrawlerBattleResultStats(key);
            brawlerBattleResultStats.countUp(Objects.requireNonNull(battle.getPlayer().getRank()));
        });
    }

    public List<BrawlerBattleRankStatisticsCollectionEntity> result() {
        return List.copyOf(cache.values());
    }


    private BrawlerBattleRankStatisticsCollectionEntity getBrawlerBattleResultStats(BrawlerBattleRankStatisticsKey key) {
        return cache.computeIfAbsent(key, k -> new BrawlerBattleRankStatisticsCollectionEntity(
                k.eventBrawlStarsId(),
                k.battleDate(),
                k.trophyRange(),
                k.brawlerBrawlStarsId()
        ));
    }
}

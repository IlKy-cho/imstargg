package com.imstargg.batch.domain;

import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerRankStatisticsCollectionEntity;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


public class BrawlerBattleRankStatisticsCollector {

    private final ConcurrentHashMap<BrawlerBattleRankStatisticsKey, BrawlerRankStatisticsCollectionEntity> cache;

    public BrawlerBattleRankStatisticsCollector() {
        this.cache = new ConcurrentHashMap<>();
    }

    public void collect(BattleCollectionEntity battle) {
        battle.playerCombinations().forEach(playerCombination -> {
            var key = BrawlerBattleRankStatisticsKey.of(battle);
            var brawlerBattleResultStats = getBrawlerBattleResultStats(key);
            brawlerBattleResultStats.countUp();
        });
    }

    public List<BrawlerRankStatisticsCollectionEntity> result() {
        return List.copyOf(cache.values());
    }


    private BrawlerRankStatisticsCollectionEntity getBrawlerBattleResultStats(BrawlerBattleRankStatisticsKey key) {
        return cache.computeIfAbsent(key, k -> new BrawlerRankStatisticsCollectionEntity(
                k.eventBrawlStarsId(),
                k.battleDate(),
                k.rank(),
                k.trophyRange(),
                k.brawlerBrawlStarsId()
        ));
    }
}

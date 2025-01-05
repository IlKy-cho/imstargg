package com.imstargg.batch.domain;

import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerBattleRankStatisticsCollectionEntity;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


public class BrawlerBattleRankStatisticsCollector {

    private final ConcurrentHashMap<BrawlerBattleRankStatisticsKey, BrawlerBattleRankStatisticsCollectionEntity> cache;

    public BrawlerBattleRankStatisticsCollector() {
        this.cache = new ConcurrentHashMap<>();
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

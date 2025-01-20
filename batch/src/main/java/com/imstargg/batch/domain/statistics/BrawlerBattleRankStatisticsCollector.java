package com.imstargg.batch.domain.statistics;

import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerBattleRankStatisticsCollectionEntity;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


public class BrawlerBattleRankStatisticsCollector
        implements StatisticsCollector<BrawlerBattleRankStatisticsCollectionEntity> {

    private final ConcurrentHashMap<BrawlerBattleRankStatisticsKey, BrawlerBattleRankStatisticsCollectionEntity> cache;

    public BrawlerBattleRankStatisticsCollector(
            ConcurrentHashMap<BrawlerBattleRankStatisticsKey, BrawlerBattleRankStatisticsCollectionEntity> cache
    ) {
        this.cache = cache;
    }

    @Override
    public boolean collect(BattleCollectionEntity battle) {
        if (!battle.canRankStatisticsCollected()) {
            return false;
        }
        battle.playerCombinations().forEach(playerCombination -> {
            var key = BrawlerBattleRankStatisticsKey.of(battle);
            var brawlerBattleResultStats = getBrawlerBattleResultStats(key);
            brawlerBattleResultStats.countUp(Objects.requireNonNull(battle.getPlayer().getRank()));
        });
        return true;
    }

    @Override
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

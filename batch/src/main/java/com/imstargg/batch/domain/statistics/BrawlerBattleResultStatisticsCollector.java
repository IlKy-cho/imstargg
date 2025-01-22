package com.imstargg.batch.domain.statistics;

import com.imstargg.core.enums.BattleResult;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerBattleResultStatisticsCollectionEntity;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

public class BrawlerBattleResultStatisticsCollector
        implements StatisticsCollector<BrawlerBattleResultStatisticsCollectionEntity> {

    private final ConcurrentMap<BrawlerBattleResultStatisticsKey, BrawlerBattleResultStatisticsCollectionEntity> cache;

    public BrawlerBattleResultStatisticsCollector(
            ConcurrentMap<BrawlerBattleResultStatisticsKey, BrawlerBattleResultStatisticsCollectionEntity> cache
    ) {
        this.cache = cache;
    }

    @Override
    public boolean collect(BattleCollectionEntity battle) {
        if (!battle.canResultStatisticsCollected()) {
            return false;
        }
        boolean isStarPlayer = battle.amIStarPlayer();
        battle.findMe().forEach(myPlayer -> {
            var key = BrawlerBattleResultStatisticsKey.of(battle, myPlayer);
            var brawlerBattleResultStats = getBrawlerBattleResultStats(key);
            brawlerBattleResultStats.countUp(BattleResult.map(battle.getResult()));
            if (isStarPlayer) {
                brawlerBattleResultStats.starPlayer();
            }
        });
        return true;
    }

    @Override
    public List<BrawlerBattleResultStatisticsCollectionEntity> result() {
        return List.copyOf(cache.values());
    }

    private BrawlerBattleResultStatisticsCollectionEntity getBrawlerBattleResultStats(BrawlerBattleResultStatisticsKey key) {
        return cache.computeIfAbsent(key, k -> new BrawlerBattleResultStatisticsCollectionEntity(
                k.eventBrawlStarsId(),
                k.battleDate(),
                k.soloRankTierRange(),
                k.trophyRange(),
                k.duplicateBrawler(),
                k.brawlerBrawlStarsId()
        ));
    }
}

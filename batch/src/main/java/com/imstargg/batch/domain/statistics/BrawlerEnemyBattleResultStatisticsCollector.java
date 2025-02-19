package com.imstargg.batch.domain.statistics;

import com.imstargg.core.enums.BattleResult;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerEnemyBattleResultStatisticsCollectionEntity;

import java.time.Clock;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

public class BrawlerEnemyBattleResultStatisticsCollector
        implements StatisticsCollector<BrawlerEnemyBattleResultStatisticsCollectionEntity> {

    private final Clock clock;
    private final ConcurrentMap<BrawlerEnemyBattleResultStatisticsKey, BrawlerEnemyBattleResultStatisticsCollectionEntity> cache;

    public BrawlerEnemyBattleResultStatisticsCollector(
            Clock clock,
            ConcurrentMap<BrawlerEnemyBattleResultStatisticsKey, BrawlerEnemyBattleResultStatisticsCollectionEntity> cache
    ) {
        this.clock = clock;
        this.cache = cache;
    }

    @Override
    public boolean collect(BattleCollectionEntity battle) {
        if (!battle.canResultStatisticsCollected()) {
            return false;
        }
        BattleResult battleResult = BattleResult.map(battle.getResult());
        battle.myPlayerCombinations().forEach(playerCombination -> {
            var key = BrawlerEnemyBattleResultStatisticsKey.of(
                    clock, battle, playerCombination.myTeamPlayer(), playerCombination.enemyTeamPlayer());
            getBrawlerEnemyBattleResultStats(key).countUp(battleResult);
        });
        return true;
    }

    @Override
    public List<BrawlerEnemyBattleResultStatisticsCollectionEntity> result() {
        return List.copyOf(cache.values());
    }

    private BrawlerEnemyBattleResultStatisticsCollectionEntity getBrawlerEnemyBattleResultStats(
            BrawlerEnemyBattleResultStatisticsKey key) {
        return cache.computeIfAbsent(key, k -> new BrawlerEnemyBattleResultStatisticsCollectionEntity(
                k.eventBrawlStarsId(),
                k.battleDate(),
                k.trophyRange(),
                k.soloRankTierRange(),
                k.brawlerBrawlStarsId(),
                k.enemyBrawlerBrawlStarsId()
        ));
    }
}

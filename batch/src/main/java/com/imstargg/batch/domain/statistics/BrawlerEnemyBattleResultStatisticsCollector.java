package com.imstargg.batch.domain.statistics;

import com.imstargg.core.enums.BattleResult;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerEnemyBattleResultStatisticsCollectionEntity;

import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class BrawlerEnemyBattleResultStatisticsCollector
        implements StatisticsCollector<BrawlerEnemyBattleResultStatisticsCollectionEntity> {

    private final ConcurrentMap<BrawlerEnemyBattleResultStatisticsKey, BrawlerEnemyBattleResultStatisticsCollectionEntity> cache;
    private final ConcurrentSkipListSet<String> battleKeySet = new ConcurrentSkipListSet<>();

    public BrawlerEnemyBattleResultStatisticsCollector(
            ConcurrentMap<BrawlerEnemyBattleResultStatisticsKey, BrawlerEnemyBattleResultStatisticsCollectionEntity> cache
    ) {
        this.cache = cache;
    }

    @Override
    public boolean collect(BattleCollectionEntity battle) {
        if (!battle.canResultStatisticsCollected() || !battleKeySet.add(battle.getBattleKey())) {
            return false;
        }
        BattleResult battleResult = BattleResult.map(battle.getResult());
        battle.playerCombinations().forEach(playerCombination -> {
            getBrawlerEnemyBattleResultStats(BrawlerEnemyBattleResultStatisticsKey
                    .of(battle, playerCombination.myTeamPlayer(), playerCombination.enemyTeamPlayer())
            ).countUp(battleResult);
            getBrawlerEnemyBattleResultStats(BrawlerEnemyBattleResultStatisticsKey
                    .of(battle, playerCombination.enemyTeamPlayer(), playerCombination.myTeamPlayer())
            ).countUp(battleResult.opposite());
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
                k.soloRankTierRange(),
                k.trophyRange(),
                k.duplicateBrawler(),
                k.brawlerBrawlStarsId(),
                k.enemyBrawlerBrawlStarsId()
        ));
    }
}

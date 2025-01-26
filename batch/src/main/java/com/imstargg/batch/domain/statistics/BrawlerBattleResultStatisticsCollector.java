package com.imstargg.batch.domain.statistics;

import com.imstargg.core.enums.BattleResult;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerBattleResultStatisticsCollectionEntity;

import java.time.Clock;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class BrawlerBattleResultStatisticsCollector
        implements StatisticsCollector<BrawlerBattleResultStatisticsCollectionEntity> {

    private final Clock clock;
    private final ConcurrentMap<BrawlerBattleResultStatisticsKey, BrawlerBattleResultStatisticsCollectionEntity> cache;
    private final ConcurrentSkipListSet<String> battleKeySet = new ConcurrentSkipListSet<>();

    public BrawlerBattleResultStatisticsCollector(
            Clock clock,
            ConcurrentMap<BrawlerBattleResultStatisticsKey, BrawlerBattleResultStatisticsCollectionEntity> cache
    ) {
        this.clock = clock;
        this.cache = cache;
    }

    @Override
    public boolean collect(BattleCollectionEntity battle) {
        if (!battle.canResultStatisticsCollected() || !battleKeySet.add(battle.getBattleKey())) {
            return false;
        }
        BattleResult battleResult = BattleResult.map(battle.getResult());
        battle.playerCombinations().forEach(playerCombination -> {
            getBrawlerBattleResultStats(BrawlerBattleResultStatisticsKey
                    .of(clock, battle, playerCombination.myTeamPlayer())
            ).countUp(battleResult);
            getBrawlerBattleResultStats(BrawlerBattleResultStatisticsKey
                    .of(clock, battle, playerCombination.enemyTeamPlayer())
            ).countUp(battleResult.opposite());
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
                k.trophyRange(),
                k.soloRankTierRange(),
                k.duplicateBrawler(),
                k.brawlerBrawlStarsId()
        ));
    }
}

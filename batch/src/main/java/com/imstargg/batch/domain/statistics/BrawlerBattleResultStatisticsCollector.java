package com.imstargg.batch.domain.statistics;

import com.imstargg.core.enums.BattleResult;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattleCollectionEntityTeamPlayer;
import com.imstargg.storage.db.core.statistics.BrawlerBattleResultStatisticsCollectionEntity;

import java.time.Clock;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * - duel 의 경우 플레이어 수가 1개 보다 많기 때문에 플레이어가 1명인 것에 대한 예외처리는 하지 않음
 */
public class BrawlerBattleResultStatisticsCollector
        implements StatisticsCollector<BrawlerBattleResultStatisticsCollectionEntity> {

    private final Clock clock;
    private final ConcurrentMap<BrawlerBattleResultStatisticsKey, BrawlerBattleResultStatisticsCollectionEntity> cache;

    public BrawlerBattleResultStatisticsCollector(
            Clock clock,
            ConcurrentMap<BrawlerBattleResultStatisticsKey, BrawlerBattleResultStatisticsCollectionEntity> cache
    ) {
        this.clock = clock;
        this.cache = cache;
    }

    @Override
    public boolean collect(BattleCollectionEntity battle) {
        if (!battle.canResultStatisticsCollected()) {
            return false;
        }
        for (BattleCollectionEntityTeamPlayer player : battle.findMe()) {
            BattleResult battleResult = BattleResult.map(battle.getResult());
            var key = BrawlerBattleResultStatisticsKey.of(clock, battle, player);
            var stats = getBrawlerBattleResultStats(key);
            stats.countUp(battleResult);
            if (battle.isStarPlayer(player)) {
                stats.starPlayer();
            }
        }

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
                k.brawlerBrawlStarsId()
        ));
    }
}

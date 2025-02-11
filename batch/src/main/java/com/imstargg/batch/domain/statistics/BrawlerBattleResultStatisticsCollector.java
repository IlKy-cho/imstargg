package com.imstargg.batch.domain.statistics;

import com.imstargg.batch.domain.SeasonEntityHolder;
import com.imstargg.core.enums.BattleResult;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattleCollectionEntityTeamPlayer;
import com.imstargg.storage.db.core.brawlstars.BrawlPassSeasonCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerBattleResultStatisticsCollectionEntity;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

public class BrawlerBattleResultStatisticsCollector
        implements StatisticsCollector<BrawlerBattleResultStatisticsCollectionEntity> {

    private final SeasonEntityHolder seasonEntityHolder;
    private final ConcurrentMap<BrawlerBattleResultStatisticsKey, BrawlerBattleResultStatisticsCollectionEntity> cache;

    public BrawlerBattleResultStatisticsCollector(
            SeasonEntityHolder seasonEntityHolder,
            ConcurrentMap<BrawlerBattleResultStatisticsKey, BrawlerBattleResultStatisticsCollectionEntity> cache
    ) {
        this.seasonEntityHolder = seasonEntityHolder;
        this.cache = cache;
    }

    @Override
    public boolean collect(BattleCollectionEntity battle) {
        BrawlPassSeasonCollectionEntity currentSeason = seasonEntityHolder.getCurrentSeasonEntity();
        if (!battle.canResultStatisticsCollected() || !currentSeason.contains(battle.getBattleTime())) {
            return false;
        }
        BattleResult battleResult = BattleResult.map(battle.getResult());
        battle.myPlayerCombinations().forEach(playerCombination -> {
            doCollect(battle, playerCombination.myTeamPlayer(), battleResult);
        });

        return true;
    }

    private void doCollect(
            BattleCollectionEntity battle, BattleCollectionEntityTeamPlayer player, BattleResult battleResult
    ) {
        var key = BrawlerBattleResultStatisticsKey.of(battle, player);
        BrawlerBattleResultStatisticsCollectionEntity stats = getBrawlerBattleResultStats(key);
        stats.countUp(battleResult);
        if (battle.isStarPlayer(player)) {
            stats.starPlayer();
        }
    }

    @Override
    public List<BrawlerBattleResultStatisticsCollectionEntity> result() {
        return List.copyOf(cache.values());
    }

    private BrawlerBattleResultStatisticsCollectionEntity getBrawlerBattleResultStats(BrawlerBattleResultStatisticsKey key) {
        return cache.computeIfAbsent(key, k -> new BrawlerBattleResultStatisticsCollectionEntity(
                seasonEntityHolder.getCurrentSeasonEntity().getNumber(),
                k.eventBrawlStarsId(),
                k.trophyRange(),
                k.soloRankTierRange(),
                k.brawlerBrawlStarsId()
        ));
    }
}

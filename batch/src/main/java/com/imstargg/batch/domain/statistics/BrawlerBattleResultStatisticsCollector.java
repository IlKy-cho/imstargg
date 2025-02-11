package com.imstargg.batch.domain.statistics;

import com.imstargg.batch.domain.SeasonEntityHolder;
import com.imstargg.core.enums.BattleResult;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattleCollectionEntityTeamPlayer;
import com.imstargg.storage.db.core.brawlstars.BrawlPassSeasonCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerBattleResultStatisticsCollectionEntity;

import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class BrawlerBattleResultStatisticsCollector
        implements StatisticsCollector<BrawlerBattleResultStatisticsCollectionEntity> {

    private final SeasonEntityHolder seasonEntityHolder;
    private final ConcurrentMap<BrawlerBattleResultStatisticsKey, BrawlerBattleResultStatisticsCollectionEntity> cache;
    private final ConcurrentSkipListSet<String> battleKeySet = new ConcurrentSkipListSet<>();

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
        if (!battle.canResultStatisticsCollected()
                || !battleKeySet.add(battle.getBattleKey()) || !currentSeason.contains(battle.getBattleTime())) {
            return false;
        }
        BattleResult battleResult = BattleResult.map(battle.getResult());
        battle.playerCombinations().forEach(playerCombination -> {
            doCollect(battle, playerCombination.myTeamPlayer(), battleResult);
            doCollect(battle, playerCombination.enemyTeamPlayer(), battleResult);
        });

        return true;
    }

    private void doCollect(
            BattleCollectionEntity battle, BattleCollectionEntityTeamPlayer player, BattleResult battleResult
    ) {
        BrawlPassSeasonCollectionEntity currentSeason = seasonEntityHolder.getCurrentSeasonEntity();
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

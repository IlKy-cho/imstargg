package com.imstargg.batch.domain.statistics;

import com.imstargg.batch.domain.SeasonEntityHolder;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.BrawlPassSeasonCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerBattleRankStatisticsCollectionEntity;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;


public class BrawlerBattleRankStatisticsCollector
        implements StatisticsCollector<BrawlerBattleRankStatisticsCollectionEntity> {

    private final SeasonEntityHolder seasonEntityHolder;
    private final ConcurrentMap<BrawlerBattleRankStatisticsKey, BrawlerBattleRankStatisticsCollectionEntity> cache;

    public BrawlerBattleRankStatisticsCollector(
            SeasonEntityHolder seasonEntityHolder,
            ConcurrentMap<BrawlerBattleRankStatisticsKey, BrawlerBattleRankStatisticsCollectionEntity> cache
    ) {
        this.seasonEntityHolder = seasonEntityHolder;
        this.cache = cache;
    }

    @Override
    public boolean collect(BattleCollectionEntity battle) {
        BrawlPassSeasonCollectionEntity currentSeason = seasonEntityHolder.getCurrentSeasonEntity();
        if (!battle.canRankStatisticsCollected() || !currentSeason.contains(battle.getBattleTime())) {
            return false;
        }
        battle.myPlayerCombinations().forEach(playerCombination -> {
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
                seasonEntityHolder.getCurrentSeasonEntity().getNumber(),
                k.eventBrawlStarsId(),
                k.trophyRange(),
                k.brawlerBrawlStarsId()
        ));
    }
}

package com.imstargg.batch.domain.statistics;

import com.imstargg.batch.domain.SeasonEntityHolder;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.BrawlPassSeasonCollectionEntity;
import com.imstargg.storage.db.core.statistics.BattleStatisticsEntityBrawlers;
import com.imstargg.storage.db.core.statistics.BrawlersBattleRankStatisticsCollectionEntity;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

public class BrawlersBattleRankStatisticsCollector
        implements StatisticsCollector<BrawlersBattleRankStatisticsCollectionEntity> {

    private final SeasonEntityHolder seasonEntityHolder;
    private final ConcurrentMap<BrawlersBattleRankStatisticsKey, BrawlersBattleRankStatisticsCollectionEntity> cache;

    public BrawlersBattleRankStatisticsCollector(
            SeasonEntityHolder seasonEntityHolder,
            ConcurrentMap<BrawlersBattleRankStatisticsKey, BrawlersBattleRankStatisticsCollectionEntity> cache
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
        battle.myTeamCombinations()
                .stream()
                .filter(myTeamCombination -> myTeamCombination.players().size() == 2)
                .forEach(myTeamCombination ->
                        BrawlersBattleRankStatisticsKey.of(battle, myTeamCombination.players()).forEach(key -> {
                            var brawlersBattleResultStats = getBrawlersBattleResult(key);
                            brawlersBattleResultStats.countUp(Objects.requireNonNull(battle.getPlayer().getRank()));
                        })
                );
        return true;
    }

    @Override
    public List<BrawlersBattleRankStatisticsCollectionEntity> result() {
        return List.copyOf(cache.values());
    }

    private BrawlersBattleRankStatisticsCollectionEntity getBrawlersBattleResult(BrawlersBattleRankStatisticsKey key) {
        return cache.computeIfAbsent(key, k -> new BrawlersBattleRankStatisticsCollectionEntity(
                seasonEntityHolder.getCurrentSeasonEntity().getNumber(),
                k.eventBrawlStarsId(),
                k.trophyRange(),
                k.brawlerBrawlStarsId(),
                new BattleStatisticsEntityBrawlers(k.brawlerBrawlStarsIdHash())
        ));
    }

}

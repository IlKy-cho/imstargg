package com.imstargg.batch.domain.statistics;

import com.imstargg.batch.domain.SeasonEntityHolder;
import com.imstargg.core.enums.BattleResult;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.BrawlPassSeasonCollectionEntity;
import com.imstargg.storage.db.core.statistics.BattleStatisticsEntityBrawlers;
import com.imstargg.storage.db.core.statistics.BrawlersBattleResultStatisticsCollectionEntity;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

public class BrawlersBattleResultStatisticsCollector
        implements StatisticsCollector<BrawlersBattleResultStatisticsCollectionEntity> {

    private final SeasonEntityHolder seasonEntityHolder;
    private final ConcurrentMap<BrawlersBattleResultStatisticsKey, BrawlersBattleResultStatisticsCollectionEntity> cache;

    public BrawlersBattleResultStatisticsCollector(
            SeasonEntityHolder seasonEntityHolder,
            ConcurrentMap<BrawlersBattleResultStatisticsKey, BrawlersBattleResultStatisticsCollectionEntity> cache
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
        battle.myPlayerTeamCombinations()
                .stream()
                .filter(myTeamCombination -> myTeamCombination.players().size() == 2)
                .forEach(myTeamCombination ->
                        BrawlersBattleResultStatisticsKey.of(battle, myTeamCombination.players()).forEach(key -> {
                            var brawlersBattleResultStats = getBrawlersBattleResult(key);
                            brawlersBattleResultStats.countUp(battleResult);
                        })
                );
        return true;
    }

    @Override
    public List<BrawlersBattleResultStatisticsCollectionEntity> result() {
        return List.copyOf(cache.values());
    }

    private BrawlersBattleResultStatisticsCollectionEntity getBrawlersBattleResult(BrawlersBattleResultStatisticsKey key) {
        return cache.computeIfAbsent(key, k -> new BrawlersBattleResultStatisticsCollectionEntity(
                seasonEntityHolder.getCurrentSeasonEntity().getNumber(),
                k.eventBrawlStarsId(),
                k.trophyRange(),
                k.soloRankTierRange(),
                k.brawlerBrawlStarsId(),
                new BattleStatisticsEntityBrawlers(k.brawlerBrawlStarsIdHash())
        ));
    }

}

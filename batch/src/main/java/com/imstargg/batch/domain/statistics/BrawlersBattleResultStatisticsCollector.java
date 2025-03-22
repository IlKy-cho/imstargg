package com.imstargg.batch.domain.statistics;

import com.imstargg.core.enums.BattleResult;
import com.imstargg.storage.db.core.player.BattleCollectionEntity;
import com.imstargg.storage.db.core.statistics.BattleStatisticsEntityBrawlers;
import com.imstargg.storage.db.core.statistics.BrawlersBattleResultStatisticsCollectionEntity;

import java.time.Clock;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

public class BrawlersBattleResultStatisticsCollector
        implements StatisticsCollector<BrawlersBattleResultStatisticsCollectionEntity> {

    private final Clock clock;
    private final ConcurrentMap<BrawlersBattleResultStatisticsKey, BrawlersBattleResultStatisticsCollectionEntity> cache;

    public BrawlersBattleResultStatisticsCollector(
            Clock clock,
            ConcurrentMap<BrawlersBattleResultStatisticsKey, BrawlersBattleResultStatisticsCollectionEntity> cache
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
        battle.myPlayerTeamCombinations()
                .stream()
                .filter(myTeamCombination -> myTeamCombination.players().size() == 2)
                .forEach(myTeamCombination ->
                        BrawlersBattleResultStatisticsKey.of(clock, battle, myTeamCombination.players()).forEach(key -> {
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
                k.eventBrawlStarsId(),
                k.battleDate(),
                k.trophyRange(),
                k.soloRankTierRange(),
                k.brawlerBrawlStarsId(),
                new BattleStatisticsEntityBrawlers(k.brawlerBrawlStarsIdHash())
        ));
    }

}

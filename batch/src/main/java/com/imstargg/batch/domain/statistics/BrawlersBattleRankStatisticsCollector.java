package com.imstargg.batch.domain.statistics;

import com.imstargg.storage.db.core.player.BattleCollectionEntity;
import com.imstargg.storage.db.core.statistics.BattleStatisticsEntityBrawlers;
import com.imstargg.storage.db.core.statistics.BrawlersBattleRankStatisticsCollectionEntity;

import java.time.Clock;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

public class BrawlersBattleRankStatisticsCollector
        implements StatisticsCollector<BrawlersBattleRankStatisticsCollectionEntity> {

    private final Clock clock;
    private final ConcurrentMap<BrawlersBattleRankStatisticsKey, BrawlersBattleRankStatisticsCollectionEntity> cache;

    public BrawlersBattleRankStatisticsCollector(
            Clock clock,
            ConcurrentMap<BrawlersBattleRankStatisticsKey, BrawlersBattleRankStatisticsCollectionEntity> cache
    ) {
        this.clock = clock;
        this.cache = cache;
    }

    @Override
    public boolean collect(BattleCollectionEntity battle) {
        if (!battle.canRankStatisticsCollected()) {
            return false;
        }
        battle.myPlayerTeamCombinations()
                .stream()
                .filter(myTeamCombination -> myTeamCombination.players().size() == 2)
                .forEach(myTeamCombination ->
                        BrawlersBattleRankStatisticsKey.of(clock, battle, myTeamCombination.players()).forEach(key -> {
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
                k.eventBrawlStarsId(),
                k.battleDate(),
                k.trophyRange(),
                k.brawlerBrawlStarsId(),
                new BattleStatisticsEntityBrawlers(k.brawlerBrawlStarsIdHash())
        ));
    }

}

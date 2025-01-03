package com.imstargg.batch.domain;

import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.statistics.BattleStatisticsCollectionEntityBrawlers;
import com.imstargg.storage.db.core.statistics.BrawlerIdHash;
import com.imstargg.storage.db.core.statistics.BrawlersRankStatisticsCollectionEntity;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


public class BrawlersBattleRankStatisticsCollector {

    private final ConcurrentHashMap<BrawlersBattleRankStatisticsKey, BrawlersRankStatisticsCollectionEntity> cache;

    public BrawlersBattleRankStatisticsCollector() {
        this.cache = new ConcurrentHashMap<>();
    }

    public void collect(BattleCollectionEntity battle) {
        battle.myTeamCombinations().forEach(myTeamCombination ->
                BrawlersBattleRankStatisticsKey.of(battle, myTeamCombination.players()).forEach(key -> {
                    var brawlersBattleResultStats = getBrawlersBattleResult(key);
                    brawlersBattleResultStats.countUp();
                })
        );
    }

    public List<BrawlersRankStatisticsCollectionEntity> result() {
        return List.copyOf(cache.values());
    }

    private BrawlersRankStatisticsCollectionEntity getBrawlersBattleResult(BrawlersBattleRankStatisticsKey key) {
        return cache.computeIfAbsent(key, k -> {
            BrawlerIdHash brawlerIdHash = new BrawlerIdHash(k.brawlerBrawlStarsIdHash());
            return new BrawlersRankStatisticsCollectionEntity(
                    k.eventBrawlStarsId(),
                    k.battleDate(),
                    k.rank(),
                    k.trophyRange(),
                    k.brawlerBrawlStarsId(),
                    new BattleStatisticsCollectionEntityBrawlers(
                            brawlerIdHash.num(),
                            brawlerIdHash.value()
                    )
            );
        });
    }

}

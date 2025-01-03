package com.imstargg.batch.domain;

import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.statistics.BattleStatisticsCollectionEntityBrawlers;
import com.imstargg.storage.db.core.statistics.BrawlerIdHash;
import com.imstargg.storage.db.core.statistics.BrawlersBattleRankStatisticsCollectionEntity;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


public class BrawlersBattleRankStatisticsCollector {

    private final ConcurrentHashMap<BrawlersBattleRankStatisticsKey, BrawlersBattleRankStatisticsCollectionEntity> cache;

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

    public List<BrawlersBattleRankStatisticsCollectionEntity> result() {
        return List.copyOf(cache.values());
    }

    private BrawlersBattleRankStatisticsCollectionEntity getBrawlersBattleResult(BrawlersBattleRankStatisticsKey key) {
        return cache.computeIfAbsent(key, k -> {
            BrawlerIdHash brawlerIdHash = new BrawlerIdHash(k.brawlerBrawlStarsIdHash());
            return new BrawlersBattleRankStatisticsCollectionEntity(
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

package com.imstargg.batch.domain;

import com.imstargg.core.enums.BattleResult;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.statistics.BattleStatisticsCollectionEntityBrawlers;
import com.imstargg.storage.db.core.statistics.BrawlerIdHash;
import com.imstargg.storage.db.core.statistics.BrawlersBattleResultStatisticsCollectionEntity;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


public class BrawlersBattleResultStatisticsCollector {

    private final ConcurrentHashMap<BrawlersBattleResultStatisticsKey, BrawlersBattleResultStatisticsCollectionEntity> cache;

    public BrawlersBattleResultStatisticsCollector() {
        this.cache = new ConcurrentHashMap<>();
    }

    public void collect(BattleCollectionEntity battle) {
        battle.myTeamCombinations()
                .stream()
                .filter(myTeamCombination -> myTeamCombination.players().size() == 2)
                .forEach(myTeamCombination ->
                BrawlersBattleResultStatisticsKey.of(battle, myTeamCombination.players()).forEach(key -> {
                    var brawlersBattleResultStats = getBrawlersBattleResult(key);
                    brawlersBattleResultStats.countUp(BattleResult.map(battle.getResult()));
                })
        );
    }

    public List<BrawlersBattleResultStatisticsCollectionEntity> result() {
        return List.copyOf(cache.values());
    }

    private BrawlersBattleResultStatisticsCollectionEntity getBrawlersBattleResult(BrawlersBattleResultStatisticsKey key) {
        return cache.computeIfAbsent(key, k -> {
            BrawlerIdHash brawlerIdHash = new BrawlerIdHash(k.brawlerBrawlStarsIdHash());
            return new BrawlersBattleResultStatisticsCollectionEntity(
                    k.eventBrawlStarsId(),
                    k.battleDate(),
                    k.soloRankTierRange(),
                    k.trophyRange(),
                    k.duplicateBrawler(),
                    k.brawlerBrawlStarsId(),
                    new BattleStatisticsCollectionEntityBrawlers(
                            brawlerIdHash.num(),
                            brawlerIdHash.value()
                    )
            );
        });
    }

}

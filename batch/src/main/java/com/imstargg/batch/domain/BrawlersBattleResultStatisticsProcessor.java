package com.imstargg.batch.domain;

import com.imstargg.core.enums.BattleResult;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.statistics.BattleStatisticsCollectionEntityBrawlers;
import com.imstargg.storage.db.core.statistics.BrawlerIdHash;
import com.imstargg.storage.db.core.statistics.BrawlersBattleResultStatisticsCollectionEntity;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


public class BrawlersBattleResultStatisticsProcessor {

    private final ConcurrentHashMap<BrawlersBattleResultKey, BrawlersBattleResultStatisticsCollectionEntity> cache;

    public BrawlersBattleResultStatisticsProcessor() {
        this.cache = new ConcurrentHashMap<>();
    }

    public void process(BattleCollectionEntity battle) {
        battle.myTeamCombinations().forEach(myTeamCombination ->
                BrawlersBattleResultKey.of(battle, myTeamCombination.players()).forEach(key -> {
                    var brawlersBattleResultStats = getBrawlersBattleResult(key);
                    brawlersBattleResultStats.countUp(BattleResult.map(battle.getResult()));
                })
        );
    }

    public List<BrawlersBattleResultStatisticsCollectionEntity> result() {
        return cache.values().stream().toList();
    }

    private BrawlersBattleResultStatisticsCollectionEntity getBrawlersBattleResult(BrawlersBattleResultKey key) {
        return cache.computeIfAbsent(key, k -> {
            BrawlerIdHash brawlerIdHash = new BrawlerIdHash(k.brawlStarsIdHash());
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

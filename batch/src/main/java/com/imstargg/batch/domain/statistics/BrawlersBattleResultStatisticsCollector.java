package com.imstargg.batch.domain.statistics;

import com.imstargg.core.enums.BattleResult;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.statistics.BattleStatisticsCollectionEntityBrawlers;
import com.imstargg.storage.db.core.statistics.BrawlerIdHash;
import com.imstargg.storage.db.core.statistics.BrawlersBattleResultStatisticsCollectionEntity;

import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class BrawlersBattleResultStatisticsCollector
        implements StatisticsCollector<BrawlersBattleResultStatisticsCollectionEntity> {

    private final ConcurrentMap<BrawlersBattleResultStatisticsKey, BrawlersBattleResultStatisticsCollectionEntity> cache;
    private final ConcurrentSkipListSet<String> battleKeySet = new ConcurrentSkipListSet<>();

    public BrawlersBattleResultStatisticsCollector(
            ConcurrentMap<BrawlersBattleResultStatisticsKey, BrawlersBattleResultStatisticsCollectionEntity> cache
    ) {
        this.cache = cache;
    }

    @Override
    public boolean collect(BattleCollectionEntity battle) {
        if (!battle.canResultStatisticsCollected() || !battleKeySet.add(battle.getBattleKey())) {
            return false;
        }
        BattleResult battleResult = BattleResult.map(battle.getResult());
        battle.myTeamCombinations()
                .stream()
                .filter(myTeamCombination -> myTeamCombination.players().size() == 2)
                .forEach(myTeamCombination ->
                        BrawlersBattleResultStatisticsKey.of(battle, myTeamCombination.players()).forEach(key -> {
                            var brawlersBattleResultStats = getBrawlersBattleResult(key);
                            brawlersBattleResultStats.countUp(battleResult);
                        })
                );
        battle.enemyTeamCombinations()
                .stream()
                .filter(enemyTeamCombination -> enemyTeamCombination.players().size() == 2)
                .forEach(enemyTeamCombination ->
                        BrawlersBattleResultStatisticsKey.of(battle, enemyTeamCombination.players()).forEach(key -> {
                            var brawlersBattleResultStats = getBrawlersBattleResult(key);
                            brawlersBattleResultStats.countUp(battleResult.opposite());
                        })
                );
        return true;
    }

    @Override
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

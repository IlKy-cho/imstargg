package com.imstargg.batch.domain;

import com.imstargg.core.enums.BattleResult;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerBattleResultStatisticsCollectionEntity;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


public class BrawlerBattleResultStatisticsProcessor {

    private final ConcurrentHashMap<BrawlerBattleResultKey, BrawlerBattleResultStatisticsCollectionEntity> cache;

    public BrawlerBattleResultStatisticsProcessor() {
        this.cache = new ConcurrentHashMap<>();
    }

    public void process(BattleCollectionEntity battle) {
        boolean isStarPlayer = battle.amIStarPlayer();
        battle.playerCombinations().forEach(playerCombination -> {
            var key = BrawlerBattleResultKey.of(battle, playerCombination.me(), playerCombination.enemy());
            var brawlerBattleResultStats = getBrawlerBattleResultStats(key);
            brawlerBattleResultStats.countUp(BattleResult.map(battle.getResult()));
            if (isStarPlayer) {
                brawlerBattleResultStats.starPlayer();
            }
        });
    }

    public List<BrawlerBattleResultStatisticsCollectionEntity> result() {
        return cache.values().stream().toList();
    }


    private BrawlerBattleResultStatisticsCollectionEntity getBrawlerBattleResultStats(BrawlerBattleResultKey key) {
        return cache.computeIfAbsent(key, k -> new BrawlerBattleResultStatisticsCollectionEntity(
                k.eventBrawlStarsId(),
                k.battleDate(),
                k.soloRankTierRange(),
                k.trophyRange(),
                k.duplicateBrawler(),
                k.brawlerBrawlStarsId(),
                k.enemyBrawlerBrawlStarsId()
        ));
    }
}

package com.imstargg.batch.domain.statistics;

import com.imstargg.batch.util.JPAQueryFactoryUtils;
import com.imstargg.core.enums.BattleResult;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerEnemyBattleResultStatisticsCollectionEntity;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.imstargg.storage.db.core.statistics.QBrawlerEnemyBattleResultStatisticsCollectionEntity.brawlerEnemyBattleResultStatisticsCollectionEntity;


public class BrawlerEnemyBattleResultStatisticsCollector {

    private final ConcurrentHashMap<BrawlerEnemyBattleResultStatisticsKey, BrawlerEnemyBattleResultStatisticsCollectionEntity> cache
            = new ConcurrentHashMap<>();

    public BrawlerEnemyBattleResultStatisticsCollector(
            EntityManagerFactory emf, LocalDate battleDate, long eventBrawlStarsId
    ) {
        JPAQueryFactoryUtils.getQueryFactory(emf)
                .selectFrom(brawlerEnemyBattleResultStatisticsCollectionEntity)
                .where(
                        brawlerEnemyBattleResultStatisticsCollectionEntity.eventBrawlStarsId.eq(eventBrawlStarsId),
                        brawlerEnemyBattleResultStatisticsCollectionEntity.battleDate.eq(battleDate)
                ).fetch()
                .forEach(entity -> cache.put(
                        BrawlerEnemyBattleResultStatisticsKey.of(entity),
                        entity
                ));
    }

    public void collect(BattleCollectionEntity battle) {
        battle.playerCombinations().forEach(playerCombination -> {
            var key = BrawlerEnemyBattleResultStatisticsKey.of(
                    battle, playerCombination.me(), playerCombination.enemy());
            var stats = getBrawlerEnemyBattleResultStats(key);
            stats.countUp(BattleResult.map(battle.getResult()));
        });
    }

    public List<BrawlerEnemyBattleResultStatisticsCollectionEntity> result() {
        return List.copyOf(cache.values());
    }


    private BrawlerEnemyBattleResultStatisticsCollectionEntity getBrawlerEnemyBattleResultStats(
            BrawlerEnemyBattleResultStatisticsKey key) {
        return cache.computeIfAbsent(key, k -> new BrawlerEnemyBattleResultStatisticsCollectionEntity(
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

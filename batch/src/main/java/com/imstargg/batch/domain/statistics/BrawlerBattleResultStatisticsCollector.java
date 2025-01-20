package com.imstargg.batch.domain.statistics;

import com.imstargg.batch.util.JPAQueryFactoryUtils;
import com.imstargg.core.enums.BattleResult;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerBattleResultStatisticsCollectionEntity;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.imstargg.storage.db.core.statistics.QBrawlerBattleResultStatisticsCollectionEntity.brawlerBattleResultStatisticsCollectionEntity;


public class BrawlerBattleResultStatisticsCollector {

    private final ConcurrentHashMap<BrawlerBattleResultStatisticsKey, BrawlerBattleResultStatisticsCollectionEntity> cache
            = new ConcurrentHashMap<>();

    public BrawlerBattleResultStatisticsCollector(
            EntityManagerFactory emf, LocalDate battleDate, long eventBrawlStarsId
    ) {
        JPAQueryFactoryUtils.getQueryFactory(emf)
                .selectFrom(brawlerBattleResultStatisticsCollectionEntity)
                .where(
                        brawlerBattleResultStatisticsCollectionEntity.eventBrawlStarsId.eq(eventBrawlStarsId),
                        brawlerBattleResultStatisticsCollectionEntity.battleDate.eq(battleDate)
                ).fetch()
                .forEach(entity -> cache.put(
                        BrawlerBattleResultStatisticsKey.of(entity),
                        entity
                ));
    }

    public void collect(BattleCollectionEntity battle) {
        boolean isStarPlayer = battle.amIStarPlayer();
        battle.findMe().forEach(myPlayer -> {
            var key = BrawlerBattleResultStatisticsKey.of(battle, myPlayer);
            var brawlerBattleResultStats = getBrawlerBattleResultStats(key);
            brawlerBattleResultStats.countUp(BattleResult.map(battle.getResult()));
            if (isStarPlayer) {
                brawlerBattleResultStats.starPlayer();
            }
        });
    }

    public List<BrawlerBattleResultStatisticsCollectionEntity> result() {
        return List.copyOf(cache.values());
    }


    private BrawlerBattleResultStatisticsCollectionEntity getBrawlerBattleResultStats(BrawlerBattleResultStatisticsKey key) {
        return cache.computeIfAbsent(key, k -> new BrawlerBattleResultStatisticsCollectionEntity(
                k.eventBrawlStarsId(),
                k.battleDate(),
                k.soloRankTierRange(),
                k.trophyRange(),
                k.duplicateBrawler(),
                k.brawlerBrawlStarsId()
        ));
    }
}

package com.imstargg.batch.domain.statistics;

import com.imstargg.batch.util.JPAQueryFactoryUtils;
import com.imstargg.storage.db.core.statistics.BrawlerEnemyBattleResultStatisticsCollectionEntity;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;

import static com.imstargg.storage.db.core.statistics.QBrawlerEnemyBattleResultStatisticsCollectionEntity.brawlerEnemyBattleResultStatisticsCollectionEntity;

public class BrawlerEnemyBattleResultStatisticsCollectorFactory
        extends StatisticsCollectorFactory<BrawlerEnemyBattleResultStatisticsCollectionEntity> {

    public BrawlerEnemyBattleResultStatisticsCollectorFactory(EntityManagerFactory emf) {
        super(emf);
    }

    @Override
    public StatisticsCollector<BrawlerEnemyBattleResultStatisticsCollectionEntity> create(
            long eventBrawlStarsId, LocalDate battleDate) {
        var cache = new ConcurrentHashMap<BrawlerEnemyBattleResultStatisticsKey, BrawlerEnemyBattleResultStatisticsCollectionEntity>();
        JPAQueryFactoryUtils.getQueryFactory(getEntityManagerFactory())
                .selectFrom(brawlerEnemyBattleResultStatisticsCollectionEntity)
                .where(
                        brawlerEnemyBattleResultStatisticsCollectionEntity.eventBrawlStarsId.eq(eventBrawlStarsId),
                        brawlerEnemyBattleResultStatisticsCollectionEntity.battleDate.eq(battleDate)
                ).fetch()
                .forEach(entity -> cache.put(
                        BrawlerEnemyBattleResultStatisticsKey.of(entity),
                        entity
                ));

        return new BrawlerEnemyBattleResultStatisticsCollector(cache);
    }
} 
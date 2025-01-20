package com.imstargg.batch.domain.statistics;

import com.imstargg.batch.util.JPAQueryFactoryUtils;
import com.imstargg.storage.db.core.statistics.BrawlerBattleResultStatisticsCollectionEntity;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;

import static com.imstargg.storage.db.core.statistics.QBrawlerBattleResultStatisticsCollectionEntity.brawlerBattleResultStatisticsCollectionEntity;

public class BrawlerBattleResultStatisticsCollectorFactory
        extends StatisticsCollectorFactory<BrawlerBattleResultStatisticsCollectionEntity> {

    public BrawlerBattleResultStatisticsCollectorFactory(EntityManagerFactory emf) {
        super(emf);
    }

    @Override
    public StatisticsCollector<BrawlerBattleResultStatisticsCollectionEntity> create(
            long eventBrawlStarsId, LocalDate battleDate) {
        var cache = new ConcurrentHashMap<BrawlerBattleResultStatisticsKey, BrawlerBattleResultStatisticsCollectionEntity>();
        JPAQueryFactoryUtils.getQueryFactory(getEntityManagerFactory())
                .selectFrom(brawlerBattleResultStatisticsCollectionEntity)
                .where(
                        brawlerBattleResultStatisticsCollectionEntity.eventBrawlStarsId.eq(eventBrawlStarsId),
                        brawlerBattleResultStatisticsCollectionEntity.battleDate.eq(battleDate)
                ).fetch()
                .forEach(entity -> cache.put(
                        BrawlerBattleResultStatisticsKey.of(entity),
                        entity
                ));

        return new BrawlerBattleResultStatisticsCollector(cache);
    }
} 
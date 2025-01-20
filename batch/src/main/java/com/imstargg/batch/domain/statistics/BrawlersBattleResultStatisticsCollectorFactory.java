package com.imstargg.batch.domain.statistics;

import com.imstargg.batch.util.JPAQueryFactoryUtils;
import com.imstargg.storage.db.core.statistics.BrawlersBattleResultStatisticsCollectionEntity;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;

import static com.imstargg.storage.db.core.statistics.QBrawlersBattleResultStatisticsCollectionEntity.brawlersBattleResultStatisticsCollectionEntity;

public class BrawlersBattleResultStatisticsCollectorFactory
        extends StatisticsCollectorFactory<BrawlersBattleResultStatisticsCollectionEntity> {

    public BrawlersBattleResultStatisticsCollectorFactory(EntityManagerFactory emf) {
        super(emf);
    }

    @Override
    public StatisticsCollector<BrawlersBattleResultStatisticsCollectionEntity> create(
            long eventBrawlStarsId, LocalDate battleDate) {
        var cache = new ConcurrentHashMap<BrawlersBattleResultStatisticsKey, BrawlersBattleResultStatisticsCollectionEntity>();
        JPAQueryFactoryUtils.getQueryFactory(getEntityManagerFactory())
                .selectFrom(brawlersBattleResultStatisticsCollectionEntity)
                .where(
                        brawlersBattleResultStatisticsCollectionEntity.eventBrawlStarsId.eq(eventBrawlStarsId),
                        brawlersBattleResultStatisticsCollectionEntity.battleDate.eq(battleDate)
                ).fetch()
                .forEach(entity -> cache.put(
                        BrawlersBattleResultStatisticsKey.of(entity),
                        entity
                ));

        return new BrawlersBattleResultStatisticsCollector(cache);
    }
} 
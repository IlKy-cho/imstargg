package com.imstargg.batch.domain.statistics;

import com.imstargg.batch.util.JPAQueryFactoryUtils;
import com.imstargg.storage.db.core.statistics.BrawlersBattleRankStatisticsCollectionEntity;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;

import static com.imstargg.storage.db.core.statistics.QBrawlersBattleRankStatisticsCollectionEntity.brawlersBattleRankStatisticsCollectionEntity;

public class BrawlersBattleRankStatisticsCollectorFactory
        extends StatisticsCollectorFactory<BrawlersBattleRankStatisticsCollectionEntity> {

    public BrawlersBattleRankStatisticsCollectorFactory(EntityManagerFactory emf) {
        super(emf);
    }

    @Override
    public StatisticsCollector<BrawlersBattleRankStatisticsCollectionEntity> create(
            long eventBrawlStarsId, LocalDate battleDate) {
        var cache = new ConcurrentHashMap<BrawlersBattleRankStatisticsKey, BrawlersBattleRankStatisticsCollectionEntity>();
        JPAQueryFactoryUtils.getQueryFactory(getEntityManagerFactory())
                .selectFrom(brawlersBattleRankStatisticsCollectionEntity)
                .where(
                        brawlersBattleRankStatisticsCollectionEntity.eventBrawlStarsId.eq(eventBrawlStarsId),
                        brawlersBattleRankStatisticsCollectionEntity.battleDate.eq(battleDate)
                ).fetch()
                .forEach(entity -> cache.put(
                        BrawlersBattleRankStatisticsKey.of(entity),
                        entity
                ));

        return new BrawlersBattleRankStatisticsCollector(cache);
    }
} 
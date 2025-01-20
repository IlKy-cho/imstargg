package com.imstargg.batch.domain.statistics;

import com.imstargg.batch.util.JPAQueryFactoryUtils;
import com.imstargg.storage.db.core.statistics.BrawlerBattleRankStatisticsCollectionEntity;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;

import static com.imstargg.storage.db.core.statistics.QBrawlerBattleRankStatisticsCollectionEntity.brawlerBattleRankStatisticsCollectionEntity;

public class BrawlerBattleRankStatisticsCollectorFactory
        extends StatisticsCollectorFactory<BrawlerBattleRankStatisticsCollectionEntity> {

    public BrawlerBattleRankStatisticsCollectorFactory(EntityManagerFactory emf) {
        super(emf);
    }

    @Override
    public StatisticsCollector<BrawlerBattleRankStatisticsCollectionEntity> create(
            long eventBrawlStarsId, LocalDate battleDate) {
                
        var cache = new ConcurrentHashMap<BrawlerBattleRankStatisticsKey, BrawlerBattleRankStatisticsCollectionEntity>();
        JPAQueryFactoryUtils.getQueryFactory(getEntityManagerFactory())
                .selectFrom(brawlerBattleRankStatisticsCollectionEntity)
                .where(
                        brawlerBattleRankStatisticsCollectionEntity.eventBrawlStarsId.eq(eventBrawlStarsId),
                        brawlerBattleRankStatisticsCollectionEntity.battleDate.eq(battleDate)
                ).fetch()
                .forEach(entity -> cache.put(
                        BrawlerBattleRankStatisticsKey.of(entity),
                        entity
                ));

        return new BrawlerBattleRankStatisticsCollector(cache);
    }
}

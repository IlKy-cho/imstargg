package com.imstargg.batch.domain.statistics;

import com.imstargg.batch.util.JPAQueryFactoryUtils;
import com.imstargg.storage.db.core.statistics.BrawlerBattleRankStatisticsCollectionEntity;
import jakarta.persistence.EntityManagerFactory;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.imstargg.storage.db.core.statistics.QBrawlerBattleRankStatisticsCollectionEntity.brawlerBattleRankStatisticsCollectionEntity;

public class BrawlerBattleRankStatisticsCollectorFactory
        implements StatisticsCollectorFactory<BrawlerBattleRankStatisticsCollectionEntity> {

    private final Clock clock;
    private final EntityManagerFactory emf;

    public BrawlerBattleRankStatisticsCollectorFactory(Clock clock, EntityManagerFactory emf) {
        this.clock = clock;
        this.emf = emf;

    }

    @Override
    public StatisticsCollector<BrawlerBattleRankStatisticsCollectionEntity> create(
            long eventBrawlStarsId, LocalDate battleDate) {

        var cache = new ConcurrentHashMap<BrawlerBattleRankStatisticsKey, BrawlerBattleRankStatisticsCollectionEntity>();
        List<BrawlerBattleRankStatisticsCollectionEntity> entities = JPAQueryFactoryUtils.getQueryFactory(emf)
                .selectFrom(brawlerBattleRankStatisticsCollectionEntity)
                .where(
                        brawlerBattleRankStatisticsCollectionEntity.eventBrawlStarsId.eq(eventBrawlStarsId),
                        brawlerBattleRankStatisticsCollectionEntity.battleDate.eq(battleDate)
                ).fetch();
        entities.forEach(BrawlerBattleRankStatisticsCollectionEntity::init);
        entities.forEach(entity -> cache.put(BrawlerBattleRankStatisticsKey.of(entity), entity));

        return new BrawlerBattleRankStatisticsCollector(clock, cache);
    }
}

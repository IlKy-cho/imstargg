package com.imstargg.batch.domain.statistics;

import com.imstargg.batch.util.JPAQueryFactoryUtils;
import com.imstargg.storage.db.core.statistics.BrawlerBattleResultStatisticsCollectionEntity;
import jakarta.persistence.EntityManagerFactory;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.imstargg.storage.db.core.statistics.QBrawlerBattleResultStatisticsCollectionEntity.brawlerBattleResultStatisticsCollectionEntity;

public class BrawlerBattleResultStatisticsCollectorFactory
        implements StatisticsCollectorFactory<BrawlerBattleResultStatisticsCollectionEntity> {

    private final Clock clock;
    private final EntityManagerFactory emf;

    public BrawlerBattleResultStatisticsCollectorFactory(Clock clock, EntityManagerFactory emf) {
        this.clock = clock;
        this.emf = emf;
    }

    @Override
    public StatisticsCollector<BrawlerBattleResultStatisticsCollectionEntity> create(
            long eventBrawlStarsId, LocalDate battleDate) {
        var cache = new ConcurrentHashMap<BrawlerBattleResultStatisticsKey, BrawlerBattleResultStatisticsCollectionEntity>();
        List<BrawlerBattleResultStatisticsCollectionEntity> entities = JPAQueryFactoryUtils.getQueryFactory(emf)
                .selectFrom(brawlerBattleResultStatisticsCollectionEntity)
                .where(
                        brawlerBattleResultStatisticsCollectionEntity.eventBrawlStarsId.eq(eventBrawlStarsId),
                        brawlerBattleResultStatisticsCollectionEntity.battleDate.eq(battleDate)
                ).fetch();
        entities.forEach(BrawlerBattleResultStatisticsCollectionEntity::init);
        entities.forEach(entity -> cache.put(BrawlerBattleResultStatisticsKey.of(entity), entity));

        return new BrawlerBattleResultStatisticsCollector(clock, cache);
    }
} 
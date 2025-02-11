package com.imstargg.batch.domain.statistics;

import com.imstargg.batch.util.JPAQueryFactoryUtils;
import com.imstargg.storage.db.core.statistics.BrawlersBattleResultStatisticsCollectionEntity;
import jakarta.persistence.EntityManagerFactory;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.imstargg.storage.db.core.statistics.QBrawlersBattleResultStatisticsCollectionEntity.brawlersBattleResultStatisticsCollectionEntity;

public class BrawlersBattleResultStatisticsCollectorFactory
        implements StatisticsCollectorFactory<BrawlersBattleResultStatisticsCollectionEntity> {

    private final Clock clock;
    private final EntityManagerFactory emf;

    public BrawlersBattleResultStatisticsCollectorFactory(
            Clock clock,
            EntityManagerFactory emf
    ) {
        this.clock = clock;
        this.emf = emf;
    }

    @Override
    public StatisticsCollector<BrawlersBattleResultStatisticsCollectionEntity> create(
            long eventBrawlStarsId, LocalDate battleDate
    ) {
        var cache = new ConcurrentHashMap<BrawlersBattleResultStatisticsKey, BrawlersBattleResultStatisticsCollectionEntity>();
        List<BrawlersBattleResultStatisticsCollectionEntity> entities = JPAQueryFactoryUtils.getQueryFactory(emf)
                .selectFrom(brawlersBattleResultStatisticsCollectionEntity)
                .where(
                        brawlersBattleResultStatisticsCollectionEntity.eventBrawlStarsId.eq(eventBrawlStarsId),
                        brawlersBattleResultStatisticsCollectionEntity.battleDate.eq(battleDate)
                ).fetch();
        entities.forEach(BrawlersBattleResultStatisticsCollectionEntity::init);
        entities.forEach(entity -> cache.put(BrawlersBattleResultStatisticsKey.of(entity), entity));

        return new BrawlersBattleResultStatisticsCollector(clock, cache);
    }
} 
package com.imstargg.batch.domain.statistics;

import com.imstargg.batch.domain.SeasonEntityHolder;
import com.imstargg.batch.util.JPAQueryFactoryUtils;
import com.imstargg.storage.db.core.statistics.BrawlersBattleResultStatisticsCollectionEntity;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.imstargg.storage.db.core.statistics.QBrawlersBattleResultStatisticsCollectionEntity.brawlersBattleResultStatisticsCollectionEntity;

public class BrawlersBattleResultStatisticsCollectorFactory
        implements StatisticsCollectorFactory<BrawlersBattleResultStatisticsCollectionEntity> {

    private final SeasonEntityHolder seasonEntityHolder;
    private final EntityManagerFactory emf;

    public BrawlersBattleResultStatisticsCollectorFactory(
            SeasonEntityHolder seasonEntityHolder, EntityManagerFactory emf) {
        this.seasonEntityHolder = seasonEntityHolder;
        this.emf = emf;
    }

    @Override
    public StatisticsCollector<BrawlersBattleResultStatisticsCollectionEntity> create(long eventBrawlStarsId) {
        var cache = new ConcurrentHashMap<BrawlersBattleResultStatisticsKey, BrawlersBattleResultStatisticsCollectionEntity>();
        List<BrawlersBattleResultStatisticsCollectionEntity> entities = JPAQueryFactoryUtils.getQueryFactory(emf)
                .selectFrom(brawlersBattleResultStatisticsCollectionEntity)
                .where(
                        brawlersBattleResultStatisticsCollectionEntity.seasonNumber
                                .eq(seasonEntityHolder.getCurrentSeasonEntity().getNumber()),
                        brawlersBattleResultStatisticsCollectionEntity.eventBrawlStarsId.eq(eventBrawlStarsId)
                ).fetch();
        entities.forEach(BrawlersBattleResultStatisticsCollectionEntity::init);
        entities.forEach(entity -> cache.put(BrawlersBattleResultStatisticsKey.of(entity), entity));

        return new BrawlersBattleResultStatisticsCollector(seasonEntityHolder, cache);
    }
} 
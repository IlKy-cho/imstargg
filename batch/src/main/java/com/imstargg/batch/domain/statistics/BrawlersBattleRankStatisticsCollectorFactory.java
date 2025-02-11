package com.imstargg.batch.domain.statistics;

import com.imstargg.batch.domain.SeasonEntityHolder;
import com.imstargg.batch.util.JPAQueryFactoryUtils;
import com.imstargg.storage.db.core.statistics.BrawlersBattleRankStatisticsCollectionEntity;
import jakarta.persistence.EntityManagerFactory;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.imstargg.storage.db.core.statistics.QBrawlersBattleRankStatisticsCollectionEntity.brawlersBattleRankStatisticsCollectionEntity;

public class BrawlersBattleRankStatisticsCollectorFactory
        implements StatisticsCollectorFactory<BrawlersBattleRankStatisticsCollectionEntity> {

    private final SeasonEntityHolder seasonEntityHolder;
    private final EntityManagerFactory emf;

    public BrawlersBattleRankStatisticsCollectorFactory(
            SeasonEntityHolder seasonEntityHolder, EntityManagerFactory emf) {
        this.seasonEntityHolder = seasonEntityHolder;
        this.emf = emf;
    }

    @Override
    public StatisticsCollector<BrawlersBattleRankStatisticsCollectionEntity> create(long eventBrawlStarsId) {

        var cache = new ConcurrentHashMap<BrawlersBattleRankStatisticsKey, BrawlersBattleRankStatisticsCollectionEntity>();
        List<BrawlersBattleRankStatisticsCollectionEntity> entities = JPAQueryFactoryUtils.getQueryFactory(emf)
                .selectFrom(brawlersBattleRankStatisticsCollectionEntity)
                .where(
                        brawlersBattleRankStatisticsCollectionEntity.seasonNumber
                                .eq(seasonEntityHolder.getCurrentSeasonEntity().getNumber()),
                        brawlersBattleRankStatisticsCollectionEntity.eventBrawlStarsId.eq(eventBrawlStarsId)
                ).fetch();
        entities.forEach(BrawlersBattleRankStatisticsCollectionEntity::init);
        entities.forEach(entity -> cache.put(BrawlersBattleRankStatisticsKey.of(entity), entity));

        return new BrawlersBattleRankStatisticsCollector(seasonEntityHolder, cache);
    }
} 
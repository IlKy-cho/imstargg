package com.imstargg.batch.domain.statistics;

import com.imstargg.batch.domain.SeasonEntityHolder;
import com.imstargg.batch.util.JPAQueryFactoryUtils;
import com.imstargg.storage.db.core.brawlstars.BrawlPassSeasonCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerBattleResultStatisticsCollectionEntity;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.imstargg.storage.db.core.statistics.QBrawlerBattleResultStatisticsCollectionEntity.brawlerBattleResultStatisticsCollectionEntity;

public class BrawlerBattleResultStatisticsCollectorFactory
        implements StatisticsCollectorFactory<BrawlerBattleResultStatisticsCollectionEntity> {

    private final SeasonEntityHolder seasonEntityHolder;
    private final EntityManagerFactory emf;

    public BrawlerBattleResultStatisticsCollectorFactory(
            SeasonEntityHolder seasonEntityHolder, EntityManagerFactory emf) {
        this.seasonEntityHolder = seasonEntityHolder;
        this.emf = emf;
    }

    @Override
    public StatisticsCollector<BrawlerBattleResultStatisticsCollectionEntity> create(long eventBrawlStarsId) {
        BrawlPassSeasonCollectionEntity currentSeason = seasonEntityHolder.getCurrentSeasonEntity();
        var cache = new ConcurrentHashMap<BrawlerBattleResultStatisticsKey, BrawlerBattleResultStatisticsCollectionEntity>();
        List<BrawlerBattleResultStatisticsCollectionEntity> entities = JPAQueryFactoryUtils.getQueryFactory(emf)
                .selectFrom(brawlerBattleResultStatisticsCollectionEntity)
                .where(
                        brawlerBattleResultStatisticsCollectionEntity.seasonNumber.eq(currentSeason.getNumber()),
                        brawlerBattleResultStatisticsCollectionEntity.eventBrawlStarsId.eq(eventBrawlStarsId)
                ).fetch();
        entities.forEach(BrawlerBattleResultStatisticsCollectionEntity::init);
        entities.forEach(entity -> cache.put(BrawlerBattleResultStatisticsKey.of(entity), entity));

        return new BrawlerBattleResultStatisticsCollector(seasonEntityHolder, cache);
    }
} 
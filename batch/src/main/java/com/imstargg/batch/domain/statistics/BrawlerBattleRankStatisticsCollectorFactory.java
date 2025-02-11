package com.imstargg.batch.domain.statistics;

import com.imstargg.batch.domain.SeasonEntityHolder;
import com.imstargg.batch.util.JPAQueryFactoryUtils;
import com.imstargg.storage.db.core.brawlstars.BrawlPassSeasonCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerBattleRankStatisticsCollectionEntity;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.imstargg.storage.db.core.statistics.QBrawlerBattleRankStatisticsCollectionEntity.brawlerBattleRankStatisticsCollectionEntity;

public class BrawlerBattleRankStatisticsCollectorFactory
        implements StatisticsCollectorFactory<BrawlerBattleRankStatisticsCollectionEntity> {

    private final SeasonEntityHolder seasonEntityHolder;
    private final EntityManagerFactory emf;

    public BrawlerBattleRankStatisticsCollectorFactory(
            SeasonEntityHolder seasonEntityHolder,  EntityManagerFactory emf
    ) {
        this.seasonEntityHolder = seasonEntityHolder;
        this.emf = emf;
    }

    @Override
    public StatisticsCollector<BrawlerBattleRankStatisticsCollectionEntity> create(long eventBrawlStarsId) {
        BrawlPassSeasonCollectionEntity currentSeason = seasonEntityHolder.getCurrentSeasonEntity();
        var cache = new ConcurrentHashMap<BrawlerBattleRankStatisticsKey, BrawlerBattleRankStatisticsCollectionEntity>();
        List<BrawlerBattleRankStatisticsCollectionEntity> entities = JPAQueryFactoryUtils.getQueryFactory(emf)
                .selectFrom(brawlerBattleRankStatisticsCollectionEntity)
                .where(
                        brawlerBattleRankStatisticsCollectionEntity.seasonNumber.eq(currentSeason.getNumber()),
                        brawlerBattleRankStatisticsCollectionEntity.eventBrawlStarsId.eq(eventBrawlStarsId)
                ).fetch();
        entities.forEach(BrawlerBattleRankStatisticsCollectionEntity::init);
        entities.forEach(entity -> cache.put(BrawlerBattleRankStatisticsKey.of(entity), entity));

        return new BrawlerBattleRankStatisticsCollector(seasonEntityHolder, cache);
    }
}

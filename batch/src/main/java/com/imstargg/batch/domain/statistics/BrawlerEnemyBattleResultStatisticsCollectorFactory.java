package com.imstargg.batch.domain.statistics;

import com.imstargg.batch.domain.SeasonEntityHolder;
import com.imstargg.batch.util.JPAQueryFactoryUtils;
import com.imstargg.storage.db.core.statistics.BrawlerEnemyBattleResultStatisticsCollectionEntity;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.imstargg.storage.db.core.statistics.QBrawlerEnemyBattleResultStatisticsCollectionEntity.brawlerEnemyBattleResultStatisticsCollectionEntity;

public class BrawlerEnemyBattleResultStatisticsCollectorFactory
        implements StatisticsCollectorFactory<BrawlerEnemyBattleResultStatisticsCollectionEntity> {

    private final SeasonEntityHolder seasonEntityHolder;
    private final EntityManagerFactory emf;

    public BrawlerEnemyBattleResultStatisticsCollectorFactory(
            SeasonEntityHolder seasonEntityHolder, EntityManagerFactory emf) {
        this.seasonEntityHolder = seasonEntityHolder;
        this.emf = emf;
    }

    @Override
    public StatisticsCollector<BrawlerEnemyBattleResultStatisticsCollectionEntity> create(long eventBrawlStarsId) {
        var cache = new ConcurrentHashMap<BrawlerEnemyBattleResultStatisticsKey, BrawlerEnemyBattleResultStatisticsCollectionEntity>();
        List<BrawlerEnemyBattleResultStatisticsCollectionEntity> entities = JPAQueryFactoryUtils.getQueryFactory(emf)
                .selectFrom(brawlerEnemyBattleResultStatisticsCollectionEntity)
                .where(
                        brawlerEnemyBattleResultStatisticsCollectionEntity.seasonNumber
                                .eq(seasonEntityHolder.getCurrentSeasonEntity().getNumber()),
                        brawlerEnemyBattleResultStatisticsCollectionEntity.eventBrawlStarsId.eq(eventBrawlStarsId)
                ).fetch();
        entities.forEach(BrawlerEnemyBattleResultStatisticsCollectionEntity::init);
        entities.forEach(entity -> cache.put(BrawlerEnemyBattleResultStatisticsKey.of(entity), entity));

        return new BrawlerEnemyBattleResultStatisticsCollector(seasonEntityHolder, cache);
    }
} 
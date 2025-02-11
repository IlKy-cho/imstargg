package com.imstargg.batch.domain.statistics;

import com.imstargg.batch.util.JPAQueryFactoryUtils;
import com.imstargg.storage.db.core.statistics.BrawlerEnemyBattleResultStatisticsCollectionEntity;
import jakarta.persistence.EntityManagerFactory;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.imstargg.storage.db.core.statistics.QBrawlerEnemyBattleResultStatisticsCollectionEntity.brawlerEnemyBattleResultStatisticsCollectionEntity;

public class BrawlerEnemyBattleResultStatisticsCollectorFactory
        implements StatisticsCollectorFactory<BrawlerEnemyBattleResultStatisticsCollectionEntity> {

    private final Clock clock;
    private final EntityManagerFactory emf;

    public BrawlerEnemyBattleResultStatisticsCollectorFactory(
            Clock clock,
            EntityManagerFactory emf
    ) {
        this.clock = clock;
        this.emf = emf;
    }

    @Override
    public StatisticsCollector<BrawlerEnemyBattleResultStatisticsCollectionEntity> create(
            long eventBrawlStarsId, LocalDate battleDate
    ) {
        var cache = new ConcurrentHashMap<BrawlerEnemyBattleResultStatisticsKey, BrawlerEnemyBattleResultStatisticsCollectionEntity>();
        List<BrawlerEnemyBattleResultStatisticsCollectionEntity> entities = JPAQueryFactoryUtils.getQueryFactory(emf)
                .selectFrom(brawlerEnemyBattleResultStatisticsCollectionEntity)
                .where(
                        brawlerEnemyBattleResultStatisticsCollectionEntity.eventBrawlStarsId.eq(eventBrawlStarsId),
                        brawlerEnemyBattleResultStatisticsCollectionEntity.battleDate.eq(battleDate)
                ).fetch();
        entities.forEach(BrawlerEnemyBattleResultStatisticsCollectionEntity::init);
        entities.forEach(entity -> cache.put(BrawlerEnemyBattleResultStatisticsKey.of(entity), entity));

        return new BrawlerEnemyBattleResultStatisticsCollector(clock, cache);
    }
} 
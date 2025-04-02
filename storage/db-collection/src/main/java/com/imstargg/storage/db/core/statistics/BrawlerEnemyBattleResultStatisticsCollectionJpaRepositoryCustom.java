package com.imstargg.storage.db.core.statistics;

import java.time.Clock;
import java.util.List;

public interface BrawlerEnemyBattleResultStatisticsCollectionJpaRepositoryCustom {

    void saveAllWithNative(Clock clock, List<BrawlerEnemyBattleResultStatisticsCollectionEntity> entities);
}

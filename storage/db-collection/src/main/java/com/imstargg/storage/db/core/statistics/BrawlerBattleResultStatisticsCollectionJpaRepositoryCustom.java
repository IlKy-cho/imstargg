package com.imstargg.storage.db.core.statistics;

import java.time.Clock;
import java.util.List;

public interface BrawlerBattleResultStatisticsCollectionJpaRepositoryCustom {

    void saveAllWithNative(Clock clock, List<BrawlerBattleResultStatisticsCollectionEntity> entities);
}

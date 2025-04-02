package com.imstargg.storage.db.core.statistics;

import java.time.Clock;
import java.util.List;

public interface BrawlerBattleRankStatisticsCollectionJpaRepositoryCustom {

    void saveAllWithNative(Clock clock, List<BrawlerBattleRankStatisticsCollectionEntity> entities);
}

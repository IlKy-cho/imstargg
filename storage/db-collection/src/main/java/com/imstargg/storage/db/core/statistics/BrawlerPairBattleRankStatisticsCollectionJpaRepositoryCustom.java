package com.imstargg.storage.db.core.statistics;

import java.time.Clock;
import java.util.List;

public interface BrawlerPairBattleRankStatisticsCollectionJpaRepositoryCustom {

    void saveAllWithNative(Clock clock, List<BrawlerPairBattleRankStatisticsCollectionEntity> entities);
}

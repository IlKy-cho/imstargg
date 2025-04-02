package com.imstargg.storage.db.core.statistics;

import java.time.Clock;
import java.util.List;

public interface BrawlerPairBattleResultStatisticsCollectionJpaRepositoryCustom {

    void saveAllWithNative(Clock clock, List<BrawlerPairBattleResultStatisticsCollectionEntity> entities);
}

package com.imstargg.storage.db.core.statistics;

import java.util.Collection;

public interface BrawlerBattleRankStatisticsCollectionJpaRepositoryCustom {

    void saveAllWithNative(Collection<BrawlerBattleRankStatisticsCollectionEntity> entities);
}

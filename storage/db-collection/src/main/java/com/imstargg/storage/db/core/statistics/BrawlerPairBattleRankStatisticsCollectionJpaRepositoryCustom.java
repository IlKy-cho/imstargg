package com.imstargg.storage.db.core.statistics;

import java.util.Collection;

public interface BrawlerPairBattleRankStatisticsCollectionJpaRepositoryCustom {

    void saveAllWithNative(Collection<BrawlerPairBattleRankStatisticsCollectionEntity> entities);
}

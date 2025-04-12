package com.imstargg.storage.db.core.statistics;

import java.util.Collection;

public interface BrawlerPairBattleResultStatisticsCollectionJpaRepositoryCustom {

    void saveAllWithNative(Collection<BrawlerPairBattleResultStatisticsCollectionEntity> entities);
}

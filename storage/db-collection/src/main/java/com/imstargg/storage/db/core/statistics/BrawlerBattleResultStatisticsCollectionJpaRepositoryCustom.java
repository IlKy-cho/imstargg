package com.imstargg.storage.db.core.statistics;

import java.util.Collection;

public interface BrawlerBattleResultStatisticsCollectionJpaRepositoryCustom {

    void saveAllWithNative(Collection<BrawlerBattleResultStatisticsCollectionEntity> entities);
}

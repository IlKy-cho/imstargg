package com.imstargg.storage.db.core.statistics;

import java.util.Collection;

public interface BrawlerEnemyBattleResultStatisticsCollectionJpaRepositoryCustom {

    void saveAllWithNative(Collection<BrawlerEnemyBattleResultStatisticsCollectionEntity> entities);
}

package com.imstargg.batch.domain.statistics;

import com.imstargg.storage.db.core.player.BattleCollectionEntity;

public interface StatisticsCollector<T> {

    boolean collect(BattleCollectionEntity battle);

    void save();
}

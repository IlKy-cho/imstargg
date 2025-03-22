package com.imstargg.batch.domain.statistics;

import com.imstargg.storage.db.core.player.BattleCollectionEntity;

import java.util.List;

public interface StatisticsCollector<T> {

    boolean collect(BattleCollectionEntity battle);

    List<T> result();
}

package com.imstargg.storage.db.core.brawlstars;

import com.imstargg.storage.db.core.BattleCollectionEntity;

import java.util.List;

public interface BattleEventCollectionJpaRepositoryCustom {

    List<BattleCollectionEntity> findAllNotRegisteredEventBattle();
}

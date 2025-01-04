package com.imstargg.storage.db.core.brawlstars;

import com.imstargg.storage.db.core.BattleCollectionEntity;

import java.util.List;
import java.util.Optional;

interface BattleEventCollectionJpaRepositoryCustom {

    List<BattleCollectionEntity> findAllNotRegisteredEventBattle();

    Optional<BattleCollectionEntity> findLatestBattle(BattleEventCollectionEntity battleEvent);
}

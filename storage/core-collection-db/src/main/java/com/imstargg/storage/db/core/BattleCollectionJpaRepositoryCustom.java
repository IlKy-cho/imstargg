package com.imstargg.storage.db.core;

import java.util.List;

interface BattleCollectionJpaRepositoryCustom {

    List<BattleCollectionEntity> findAllLastBattleByPlayerIdIn(List<Long> playerIds);
}

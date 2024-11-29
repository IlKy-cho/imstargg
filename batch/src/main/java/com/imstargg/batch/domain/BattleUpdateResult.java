package com.imstargg.batch.domain;

import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattlePlayerCollectionEntity;

import java.util.List;

public record BattleUpdateResult(
        BattleCollectionEntity battleEntity,
        List<BattlePlayerCollectionEntity> battlePlayerEntities
) {
}

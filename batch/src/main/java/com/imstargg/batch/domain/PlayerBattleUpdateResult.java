package com.imstargg.batch.domain;

import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.PlayerCollectionEntity;

import java.util.List;

public record PlayerBattleUpdateResult(
        PlayerCollectionEntity playerEntity,
        List<BattleCollectionEntity> battleEntities
) {
}

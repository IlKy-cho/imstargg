package com.imstargg.batch.domain;

import com.imstargg.storage.db.core.player.BattleCollectionEntity;
import com.imstargg.storage.db.core.player.PlayerCollectionEntity;

import java.util.List;

public record PlayerBattleUpdateResult(
        PlayerCollectionEntity playerEntity,
        List<BattleCollectionEntity> battleEntities
) {
}

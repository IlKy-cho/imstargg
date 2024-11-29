package com.imstargg.batch.domain;

import com.imstargg.storage.db.core.PlayerBrawlerCollectionEntity;
import com.imstargg.storage.db.core.PlayerCollectionEntity;

import java.util.List;

public record PlayerUpdatedEntity(
        PlayerCollectionEntity playerEntity,
        List<PlayerBrawlerCollectionEntity> playerBrawlerEntities,
        List<BattleUpdateResult> battleUpdateResults
) {
}

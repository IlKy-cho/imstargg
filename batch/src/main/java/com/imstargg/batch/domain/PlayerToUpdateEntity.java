package com.imstargg.batch.domain;

import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.PlayerBrawlerCollectionEntity;
import com.imstargg.storage.db.core.PlayerCollectionEntity;

import java.util.List;
import java.util.Optional;

public record PlayerToUpdateEntity(
        PlayerCollectionEntity playerEntity,
        List<PlayerBrawlerCollectionEntity> playerBrawlerEntities,
        Optional<BattleCollectionEntity> lastBattleEntity
) {

    public PlayerToUpdateEntity(
            PlayerCollectionEntity playerEntity, List<PlayerBrawlerCollectionEntity> playerBrawlerEntities) {
        this(playerEntity, playerBrawlerEntities, Optional.empty());
    }
}

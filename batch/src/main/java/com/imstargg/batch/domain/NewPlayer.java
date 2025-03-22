package com.imstargg.batch.domain;

import com.imstargg.storage.db.core.player.PlayerCollectionEntity;
import com.imstargg.storage.db.core.player.UnknownPlayerCollectionEntity;
import jakarta.annotation.Nullable;

public record NewPlayer(
        UnknownPlayerCollectionEntity unknownPlayerEntity,
        @Nullable PlayerCollectionEntity playerEntity
) {
}

package com.imstargg.batch.domain;

import com.imstargg.storage.db.core.PlayerCollectionEntity;
import com.imstargg.storage.db.core.UnknownPlayerCollectionEntity;
import jakarta.annotation.Nullable;

public record NewPlayer(
        UnknownPlayerCollectionEntity unknownPlayerEntity,
        @Nullable PlayerCollectionEntity playerEntity
) {
}

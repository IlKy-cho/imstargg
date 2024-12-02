package com.imstargg.batch.domain;

import com.imstargg.storage.db.core.PlayerCollectionEntity;
import com.imstargg.storage.db.core.UnknownPlayerCollectionEntity;

public record NewPlayer(
        UnknownPlayerCollectionEntity unknownPlayerEntity,
        PlayerCollectionEntity playerEntity
) {
}

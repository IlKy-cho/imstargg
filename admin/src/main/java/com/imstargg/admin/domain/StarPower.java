package com.imstargg.admin.domain;

import com.imstargg.storage.db.core.MessageCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.StarPowerCollectionEntity;
import jakarta.annotation.Nullable;

import java.util.List;

public record StarPower(
        StarPowerCollectionEntity entity,
        List<MessageCollectionEntity> names,
        @Nullable BrawlStarsImageCollectionEntity image
) {
}

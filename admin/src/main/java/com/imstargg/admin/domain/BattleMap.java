package com.imstargg.admin.domain;

import com.imstargg.storage.db.core.MessageCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.BattleEventCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.BattleMapCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageCollectionEntity;
import jakarta.annotation.Nullable;

import java.util.List;

public record BattleMap(
        BattleMapCollectionEntity entity,
        List<MessageCollectionEntity> names,
        @Nullable BrawlStarsImageCollectionEntity image,
        List<BattleEventCollectionEntity> events
) {
}

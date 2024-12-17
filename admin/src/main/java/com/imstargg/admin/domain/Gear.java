package com.imstargg.admin.domain;

import com.imstargg.storage.db.core.MessageCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.GearCollectionEntity;

import java.util.List;

public record Gear(
        GearCollectionEntity entity,
        List<MessageCollectionEntity> names
) {
}

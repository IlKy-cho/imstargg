package com.imstargg.admin.domain;

import com.imstargg.storage.db.core.MessageCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.GadgetCollectionEntity;

import java.util.List;

public record Gadget(
        GadgetCollectionEntity entity,
        List<MessageCollectionEntity> names
) {
}

package com.imstargg.admin.domain;

import com.imstargg.storage.db.core.MessageCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.BrawlerCollectionEntity;
import jakarta.annotation.Nullable;

import java.util.List;

public record Brawler(
        BrawlerCollectionEntity entity,
        List<MessageCollectionEntity> names,
        @Nullable BrawlStarsImageCollectionEntity image,
        List<Gadget> gadgets,
        List<StarPower> starPowers,
        List<Gear> gears
) {
}

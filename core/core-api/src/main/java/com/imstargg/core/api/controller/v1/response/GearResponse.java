package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.brawlstars.Gear;
import com.imstargg.core.enums.GearRarity;
import jakarta.annotation.Nullable;

public record GearResponse(
        long id,
        String name,
        GearRarity rarity,
        @Nullable String imagePath
) {

    public static GearResponse from(Gear gear) {
        return new GearResponse(
                gear.id().value(),
                gear.name(),
                gear.rarity(),
                gear.imagePath()
        );
    }
}

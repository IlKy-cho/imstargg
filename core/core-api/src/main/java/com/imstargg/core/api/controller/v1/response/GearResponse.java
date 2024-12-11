package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.brawlstars.Gear;
import com.imstargg.core.enums.GearRarity;

public record GearResponse(
        long id,
        String name,
        GearRarity rarity
) {

    public static GearResponse from(Gear gear) {
        return new GearResponse(
                gear.id().value(),
                gear.name(),
                gear.rarity()
        );
    }
}

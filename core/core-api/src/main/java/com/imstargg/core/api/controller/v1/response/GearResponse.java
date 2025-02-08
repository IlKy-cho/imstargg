package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.brawlstars.Gear;
import com.imstargg.core.enums.GearRarity;
import com.imstargg.core.enums.Language;
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
                gear.names()
                        .find(Language.KOREAN)
                        .orElseThrow(() -> new IllegalStateException(
                                "기어 이름이 존재하지 않습니다. gear id: " + gear.id().value()))
                        .content(),
                gear.rarity(),
                gear.imagePath()
        );
    }
}

package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.brawlstars.Brawler;
import com.imstargg.core.enums.BrawlerRarity;
import com.imstargg.core.enums.BrawlerRole;
import jakarta.annotation.Nullable;

import java.util.List;

public record BrawlerResponse(
        long id,
        String name,
        BrawlerRarity rarity,
        BrawlerRole role,
        List<GadgetResponse> gadgets,
        List<GearResponse> gears,
        List<StarPowerResponse> starPowers,
        @Nullable String imageUrl
) {

    public static BrawlerResponse from(Brawler brawler) {
        return new BrawlerResponse(
                brawler.id().value(),
                brawler.name(),
                brawler.rarity(),
                brawler.role(),
                brawler.gadgets().stream().map(GadgetResponse::from).toList(),
                brawler.gears().stream().map(GearResponse::from).toList(),
                brawler.starPowers().stream().map(StarPowerResponse::from).toList(),
                brawler.imageUrl()
        );
    }
}

package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.brawlstars.Brawler;
import com.imstargg.core.domain.brawlstars.Gadget;
import com.imstargg.core.domain.brawlstars.Gear;
import com.imstargg.core.domain.brawlstars.StarPower;
import com.imstargg.core.enums.BrawlerRarity;

import java.util.List;

public record BrawlerResponse(
        long id,
        String name,
        BrawlerRarity rarity,
        List<Gadget> gadgets,
        List<Gear> gears,
        List<StarPower> starPowers
) {

    public static BrawlerResponse from(Brawler brawler) {
        return new BrawlerResponse(
                brawler.id().value(),
                brawler.name(),
                brawler.rarity(),
                brawler.gadgets(),
                brawler.gears(),
                brawler.starPowers()
        );
    }
}

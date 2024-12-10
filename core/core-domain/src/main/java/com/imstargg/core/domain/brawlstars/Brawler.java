package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.enums.BrawlerRarity;

import java.util.List;

public record Brawler(
        BrawlStarsId id,
        String name,
        BrawlerRarity rarity,
        List<Gadget> gadgets,
        List<Gear> gears,
        List<StarPower> starPowers
) {

}

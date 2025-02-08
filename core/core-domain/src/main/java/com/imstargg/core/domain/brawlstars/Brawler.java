package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.MessageCollection;
import com.imstargg.core.enums.BrawlerRarity;
import com.imstargg.core.enums.BrawlerRole;
import jakarta.annotation.Nullable;

import java.util.List;

public record Brawler(
        BrawlStarsId id,
        MessageCollection names,
        BrawlerRarity rarity,
        BrawlerRole role,
        List<Gadget> gadgets,
        List<Gear> gears,
        List<StarPower> starPowers,
        @Nullable String imagePath
) {

}

package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.enums.GearRarity;

public record Gear(
        BrawlStarsId id,
        GearRarity rarity,
        String name
) {
}

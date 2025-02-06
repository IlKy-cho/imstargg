package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.enums.GearRarity;
import jakarta.annotation.Nullable;

public record Gear(
        BrawlStarsId id,
        GearRarity rarity,
        String name,
        @Nullable String imagePath
) {
}

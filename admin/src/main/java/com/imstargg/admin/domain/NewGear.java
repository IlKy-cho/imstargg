package com.imstargg.admin.domain;

import com.imstargg.core.enums.GearRarity;

public record NewGear(
        long brawlStarsId,
        NewMessageCollection names,
        GearRarity rarity
) {
}

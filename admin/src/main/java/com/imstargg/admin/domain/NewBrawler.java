package com.imstargg.admin.domain;

import com.imstargg.core.enums.BrawlerRarity;
import com.imstargg.core.enums.BrawlerRole;

import java.util.List;

public record NewBrawler(
        long brawlStarsId,
        BrawlerRarity rarity,
        BrawlerRole role,
        NewMessageCollection names,
        List<Long> gearIds,
        List<NewGadget> gadgets,
        List<NewStarPower> starPowers
) {
}

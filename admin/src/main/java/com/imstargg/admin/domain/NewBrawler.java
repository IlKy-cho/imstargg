package com.imstargg.admin.domain;

import com.imstargg.core.enums.BrawlerRarity;
import com.imstargg.core.enums.BrawlerRole;

public record NewBrawler(
        long brawlStarsId,
        BrawlerRarity rarity,
        BrawlerRole role,
        NewMessageCollection names
) {

}

package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.brawlstars.Brawler;
import com.imstargg.core.enums.BrawlerRarity;

public record BattlePlayerBrawlerResponse(
        long id,
        String name,
        BrawlerRarity rarity
) {

    public static BattlePlayerBrawlerResponse from(Brawler brawler) {
        return new BattlePlayerBrawlerResponse(
                brawler.id().value(),
                brawler.name(),
                brawler.rarity()
        );
    }
}

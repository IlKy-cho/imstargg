package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.BattlePlayerBrawler;
import jakarta.annotation.Nullable;

public record BattlePlayerBrawlerResponse(
        @Nullable Long id,
        @Nullable String name,
        @Nullable String imageUrl,
        int brawlerPower,
        @Nullable Integer brawlerTrophies,
        @Nullable Integer brawlerTrophyChange
) {

    public static BattlePlayerBrawlerResponse from(BattlePlayerBrawler brawler) {
        return new BattlePlayerBrawlerResponse(
                brawler.brawler() == null ? null : brawler.brawler().id().value(),
                brawler.brawler() == null ? null : brawler.brawler().name(),
                brawler.brawler() == null ? null : brawler.brawler().imageUrl(),
                brawler.power(),
                brawler.trophies(),
                brawler.trophyChange()
        );
    }
}

package com.imstargg.core.api.controller.v1.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imstargg.core.domain.BattlePlayerBrawler;
import jakarta.annotation.Nullable;

public record BattlePlayerBrawlerResponse(
        @Nullable Long id,
        int brawlerPower,
        @JsonInclude(JsonInclude.Include.NON_NULL) @Nullable Integer brawlerTrophies,
        @JsonInclude(JsonInclude.Include.NON_NULL) @Nullable Integer brawlerTrophyChange
) {

    public static BattlePlayerBrawlerResponse from(BattlePlayerBrawler brawler) {
        return new BattlePlayerBrawlerResponse(
                brawler.brawler() == null ? null : brawler.brawler().id().value(),
                brawler.power(),
                brawler.trophies(),
                brawler.trophyChange()
        );
    }
}

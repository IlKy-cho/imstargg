package com.imstargg.core.api.controller.v1.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imstargg.core.domain.BattlePlayer;
import com.imstargg.core.enums.SoloRankTier;
import jakarta.annotation.Nullable;

public record BattlePlayerResponse(
        String tag,
        String name,
        @JsonInclude(JsonInclude.Include.NON_NULL) @Nullable SoloRankTier soloRankTier,
        BattlePlayerBrawlerResponse brawler
) {

    public static BattlePlayerResponse from(BattlePlayer battlePlayer) {
        return new BattlePlayerResponse(
                battlePlayer.tag().value(),
                battlePlayer.name(),
                battlePlayer.soloRankTier(),
                BattlePlayerBrawlerResponse.from(battlePlayer.brawler())
        );
    }
}

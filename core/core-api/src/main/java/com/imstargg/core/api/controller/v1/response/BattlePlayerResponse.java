package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.BattlePlayer;
import jakarta.annotation.Nullable;

public record BattlePlayerResponse(
        String tag,
        String name,
        @Nullable BattlePlayerBrawlerResponse brawler,
        int brawlerPower,
        @Nullable Integer brawlerTrophies,
        @Nullable Integer brawlerTrophyChange
) {

    public static BattlePlayerResponse from(BattlePlayer battlePlayer) {
        return new BattlePlayerResponse(
                battlePlayer.tag().value(),
                battlePlayer.name(),
                battlePlayer.brawler() == null ? null : BattlePlayerBrawlerResponse.from(battlePlayer.brawler()),
                battlePlayer.brawlerPower(),
                battlePlayer.brawlerTrophies(),
                battlePlayer.brawlerTrophyChange()
        );
    }
}

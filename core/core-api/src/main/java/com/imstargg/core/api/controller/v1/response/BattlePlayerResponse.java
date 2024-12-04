package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.BattlePlayer;
import com.imstargg.core.enums.Brawler;
import jakarta.annotation.Nullable;

public record BattlePlayerResponse(
        String tag,
        String name,
        Brawler brawler,
        int brawlerPower,
        @Nullable Integer brawlerTrophies,
        @Nullable Integer brawlerTrophyChange
) {

    public static BattlePlayerResponse from(BattlePlayer battlePlayer) {
        return new BattlePlayerResponse(
                battlePlayer.tag().value(),
                battlePlayer.name(),
                battlePlayer.brawler(),
                battlePlayer.brawlerPower(),
                battlePlayer.brawlerTrophies(),
                battlePlayer.brawlerTrophyChange()
        );
    }
}

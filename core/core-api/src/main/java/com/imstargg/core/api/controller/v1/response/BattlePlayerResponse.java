package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.BattlePlayer;

public record BattlePlayerResponse(
        String tag,
        String name,
        BattlePlayerBrawlerResponse brawler
) {

    public static BattlePlayerResponse from(BattlePlayer battlePlayer) {
        return new BattlePlayerResponse(
                battlePlayer.tag().value(),
                battlePlayer.name(),
                BattlePlayerBrawlerResponse.from(battlePlayer.brawler())
        );
    }
}

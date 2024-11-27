package com.imstargg.client.brawlstars.response;

import jakarta.annotation.Nullable;

public record BattleResultBrawlerResponse(
        long id,
        String name,
        int power,
        int trophies,
        @Nullable Integer trophyChange
) {
}

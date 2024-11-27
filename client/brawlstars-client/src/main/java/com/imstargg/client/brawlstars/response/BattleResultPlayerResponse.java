package com.imstargg.client.brawlstars.response;

import jakarta.annotation.Nullable;

import java.util.List;

public record BattleResultPlayerResponse(
        String tag,
        String name,
        @Nullable BattleResultBrawlerResponse brawler,
        @Nullable List<BattleResultBrawlerResponse> brawlers
) {
}

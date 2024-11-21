package com.imstargg.client.brawlstars.response;

public record BattleResultPlayerResponse(
        String tag,
        String name,
        BattleResultBrawlerResponse brawler
) {
}

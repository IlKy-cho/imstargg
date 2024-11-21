package com.imstargg.client.brawlstars.response;

public record BattleResultBrawlerResponse(
        long id,
        String name,
        int power,
        int trophies
) {
}

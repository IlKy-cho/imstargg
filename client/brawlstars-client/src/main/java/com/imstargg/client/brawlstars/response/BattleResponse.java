package com.imstargg.client.brawlstars.response;

public record BattleResponse(
        String battleTime,
        EventResponse event,
        BattleResultResponse battle
) {
}

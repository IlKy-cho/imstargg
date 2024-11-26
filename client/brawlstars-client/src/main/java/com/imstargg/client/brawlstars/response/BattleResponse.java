package com.imstargg.client.brawlstars.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;

public record BattleResponse(
        @JsonDeserialize(using = BrawlStarsLocalDateTimeSerializer.class)
        LocalDateTime battleTime,
        EventResponse event,
        BattleResultResponse battle
) {
}

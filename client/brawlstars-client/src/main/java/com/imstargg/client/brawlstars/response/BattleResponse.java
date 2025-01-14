package com.imstargg.client.brawlstars.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.OffsetDateTime;

public record BattleResponse(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd'T'HHmmss.SSSX")
        OffsetDateTime battleTime,
        EventResponse event,
        BattleResultResponse battle
) {
}

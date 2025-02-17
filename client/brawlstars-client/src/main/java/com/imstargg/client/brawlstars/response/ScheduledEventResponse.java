package com.imstargg.client.brawlstars.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.OffsetDateTime;

public record ScheduledEventResponse(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd'T'HHmmss.SSSX")
        OffsetDateTime startTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd'T'HHmmss.SSSX")
        OffsetDateTime endTime,
        long slotId,
        ScheduledEventLocationResponse event
) {
}

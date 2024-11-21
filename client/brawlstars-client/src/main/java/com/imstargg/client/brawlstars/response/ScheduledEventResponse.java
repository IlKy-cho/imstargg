package com.imstargg.client.brawlstars.response;

public record ScheduledEventResponse(
        String startTime,
        String endTime,
        long slotId,
        ScheduledEventLocationResponse event
) {
}

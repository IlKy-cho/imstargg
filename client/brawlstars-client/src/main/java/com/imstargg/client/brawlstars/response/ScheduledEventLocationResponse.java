package com.imstargg.client.brawlstars.response;

import jakarta.annotation.Nullable;

import java.util.List;

public record ScheduledEventLocationResponse(
        long id,
        String mode,
        String map,
        @Nullable List<String> modifiers
) {
}

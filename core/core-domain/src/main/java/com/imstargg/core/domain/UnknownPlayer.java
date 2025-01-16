package com.imstargg.core.domain;

import java.time.Clock;
import java.time.LocalDateTime;

public record UnknownPlayer(
        BrawlStarsTag tag,
        int notFoundCount,
        LocalDateTime updatedAt
) {

    public boolean updateAvailable(Clock clock) {
        LocalDateTime updateAvailableAt = updatedAt.plusMinutes(2L * notFoundCount);
        return LocalDateTime.now(clock).isAfter(updateAvailableAt);
    }
}

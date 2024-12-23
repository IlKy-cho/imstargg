package com.imstargg.core.domain;

import com.imstargg.core.enums.UnknownPlayerStatus;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;

public record UnknownPlayer(
        BrawlStarsTag tag,
        UnknownPlayerStatus status,
        LocalDateTime updateAvailableAt
) {

    public boolean updateAvailable(Clock clock) {
        boolean timeAvailable = updateAvailableAt().isBefore(LocalDateTime.now(clock));
        if (!timeAvailable) {
            return false;
        }

        if (status == UnknownPlayerStatus.UPDATE_NEW || status == UnknownPlayerStatus.ADMIN_NEW) {
            return true;
        }

        if (status == UnknownPlayerStatus.SEARCH_NEW
                && Duration.between(updateAvailableAt(), LocalDateTime.now(clock)).toSeconds() > 120) {
            return true;
        }

        return false;
    }
}

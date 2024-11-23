package com.imstargg.core.enums;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;

public enum PlayerStatus {

    UPDATE_NEW,
    REFRESH_NEW,
    NOT_FOUND,
    REFRESH_REQUESTED,
    UPDATED,
    DELETED,
    ;

    private static final Duration NEXT_UPDATABLE_TERM = Duration.ofSeconds(120);

    public boolean isUpdatable(Clock clock, LocalDateTime lastUpdatedAt) {
        if (UPDATE_NEW == this || REFRESH_NEW == this || REFRESH_REQUESTED == this) {
            return true;
        }
        if (DELETED == this) {
            return false;
        }
        return Duration.between(lastUpdatedAt, LocalDateTime.now(clock)).compareTo(NEXT_UPDATABLE_TERM) > 0;
    }
}

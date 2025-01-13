package com.imstargg.core.enums;

import java.time.Duration;
import java.time.LocalDateTime;

public enum PlayerStatus {

    NEW,
    PLAYER_UPDATED,
    DORMANT,
    DORMANT_RETURNED,
    DELETED,
    ;

    private static final Duration NEXT_UPDATABLE_TERM = Duration.ofSeconds(120);

    public boolean isNextUpdateCooldownOver(LocalDateTime now, LocalDateTime updatedAt) {
        return updatedAt.plus(NEXT_UPDATABLE_TERM).isBefore(now);
    }
}


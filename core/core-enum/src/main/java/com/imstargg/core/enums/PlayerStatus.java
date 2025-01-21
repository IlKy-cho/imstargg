package com.imstargg.core.enums;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

public enum PlayerStatus {

    NEW,
    PLAYER_UPDATED,
    DORMANT,
    DORMANT_RETURNED,
    DELETED,
    ;

    private static final Duration NEXT_UPDATABLE_TERM = Duration.ofSeconds(120);

    public static List<PlayerStatus> updatableStatuses() {
        return Arrays.stream(values())
                .filter(PlayerStatus::isUpdatable)
                .toList();
    }

    public boolean isUpdatable() {
        return this == NEW || this == DORMANT || this == DORMANT_RETURNED;
    }

    public boolean isNextUpdateCooldownOver(OffsetDateTime now, OffsetDateTime updatedAt) {
        return updatedAt.plus(NEXT_UPDATABLE_TERM).isBefore(now);
    }
}


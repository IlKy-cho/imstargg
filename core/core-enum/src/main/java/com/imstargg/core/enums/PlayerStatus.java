package com.imstargg.core.enums;

import java.util.Arrays;
import java.util.List;

public enum PlayerStatus {

    NEW,
    PLAYER_UPDATED,
    DORMANT,
    DORMANT_RETURNED,
    DELETED,
    ;

    public static List<PlayerStatus> updatableStatuses() {
        return Arrays.stream(values())
                .filter(PlayerStatus::isUpdatable)
                .toList();
    }

    public boolean isUpdatable() {
        return this == NEW || this == DORMANT || this == DORMANT_RETURNED;
    }
}


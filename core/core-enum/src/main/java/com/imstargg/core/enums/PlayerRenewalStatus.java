package com.imstargg.core.enums;

import java.util.Arrays;
import java.util.List;

public enum PlayerRenewalStatus {

    NEW,
    PENDING,
    EXECUTING,
    COMPLETE,
    IN_MAINTENANCE,
    FAILED
    ;

    public static List<PlayerRenewalStatus> renewingList() {
        return Arrays.stream(values())
                .filter(PlayerRenewalStatus::renewing)
                .toList();
    }

    public boolean finished() {
        return this == COMPLETE || this == FAILED || this == IN_MAINTENANCE;
    }

    public boolean renewing() {
        return this == PENDING || this == EXECUTING;
    }
}

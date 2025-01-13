package com.imstargg.core.enums;

import java.util.Arrays;
import java.util.List;

public enum PlayerRenewalStatus {

    PENDING,
    EXECUTING,
    COMPLETED,
    FAILED
    ;

    public static List<PlayerRenewalStatus> renewingList() {
        return Arrays.stream(values())
                .filter(PlayerRenewalStatus::renewing)
                .toList();
    }

    public boolean finished() {
        return this == COMPLETED || this == FAILED;
    }

    public boolean renewing() {
        return this == PENDING || this == EXECUTING;
    }
}

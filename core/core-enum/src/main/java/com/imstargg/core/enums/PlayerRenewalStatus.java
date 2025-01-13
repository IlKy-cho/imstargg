package com.imstargg.core.enums;

import java.util.Arrays;
import java.util.List;

public enum PlayerRenewalStatus {

    PENDING,
    EXECUTING,
    COMPLETE,
    FAILED
    ;

    public static List<PlayerRenewalStatus> renewingList() {
        return Arrays.stream(values())
                .filter(PlayerRenewalStatus::renewing)
                .toList();
    }

    public boolean finished() {
        return this == COMPLETE || this == FAILED;
    }

    public boolean renewing() {
        return this == PENDING || this == EXECUTING;
    }
}

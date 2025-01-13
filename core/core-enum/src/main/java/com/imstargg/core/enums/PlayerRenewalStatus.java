package com.imstargg.core.enums;

public enum PlayerRenewalStatus {

    PENDING,
    EXECUTING,
    COMPLETED,
    FAILED
    ;

    public boolean finished() {
        return this == COMPLETED || this == FAILED;
    }

    public boolean renewing() {
        return this == PENDING || this == EXECUTING;
    }
}

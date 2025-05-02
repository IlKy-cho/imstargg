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

    /**
     * worker 의 플레이어 갱신 중 executing 상태로 업데이트하는 것이 트랜잭션으로 묶여있지 않아서,
     * 재시도 발생을 대비하여 executing 시에도 renew 가능하도록 한다.
     */
    public boolean canRenew() {
        return this == PENDING || this == EXECUTING;
    }
}

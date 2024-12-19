package com.imstargg.core.enums;

import java.time.Duration;
import java.time.LocalDateTime;

public enum PlayerStatus {

    NEW(true),    // 새로 생성된 상태. 전적이 저장되어있지 않음.
    PLAYER_UPDATED(false),  // 플레이어 정보 및 전적이 업데이트된 상태
    BATTLE_UPDATED(true),   // 배치를 통해 전적이 업데이트된 상태
    DORMANT(false),
    DORMANT_RETURNED(false),
    RENEW_REQUESTED(true),
    DELETED(false),
    ;

    private final boolean renewing;

    PlayerStatus(boolean renewing) {
        this.renewing = renewing;
    }

    private static final Duration NEXT_UPDATABLE_TERM = Duration.ofSeconds(120);

    public boolean isNextUpdateCooldownOver(LocalDateTime now, LocalDateTime updatedAt) {
        return updatedAt.plus(NEXT_UPDATABLE_TERM).isBefore(now);
    }

    public boolean isRenewing() {
        return renewing;
    }
}


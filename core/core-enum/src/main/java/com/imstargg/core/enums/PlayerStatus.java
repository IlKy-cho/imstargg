package com.imstargg.core.enums;

import java.time.Duration;
import java.time.LocalDateTime;

public enum PlayerStatus {

    NEW,    // 새로 생성된 상태. 전적이 저장되어있지 않음.
    PLAYER_UPDATED,  // 플레이어 정보 및 전적이 업데이트된 상태
    BATTLE_UPDATED,   // 배치를 통해 전적이 업데이트된 상태
    DORMANT,
    DORMANT_RETURNED,
    RENEW_REQUESTED,
    DELETED,
    ;

    private static final Duration NEXT_UPDATABLE_TERM = Duration.ofSeconds(120);

    public boolean isNextUpdateCooldownOver(LocalDateTime now, LocalDateTime updatedAt) {
        return updatedAt.plus(NEXT_UPDATABLE_TERM).isBefore(now);
    }
}


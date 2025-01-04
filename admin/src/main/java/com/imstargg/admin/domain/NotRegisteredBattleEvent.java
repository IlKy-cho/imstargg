package com.imstargg.admin.domain;

import jakarta.annotation.Nullable;

import java.time.LocalDateTime;

public record NotRegisteredBattleEvent(
        long brawlStarsId,
        @Nullable String mode,
        @Nullable String map,
        LocalDateTime battleTime
) {
}

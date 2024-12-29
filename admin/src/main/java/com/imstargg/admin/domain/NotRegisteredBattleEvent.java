package com.imstargg.admin.domain;

import jakarta.annotation.Nullable;

public record NotRegisteredBattleEvent(
        long brawlStarsId,
        @Nullable String mode,
        @Nullable String map
) {
}

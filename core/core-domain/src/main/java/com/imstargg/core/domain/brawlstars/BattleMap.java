package com.imstargg.core.domain.brawlstars;

import jakarta.annotation.Nullable;

public record BattleMap(
        String code,
        String name,
        @Nullable String imageUrl
) {
}

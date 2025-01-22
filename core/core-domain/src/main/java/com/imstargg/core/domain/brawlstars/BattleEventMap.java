package com.imstargg.core.domain.brawlstars;

import jakarta.annotation.Nullable;

public record BattleEventMap(
        @Nullable String name,
        @Nullable String imagePath
) {
}

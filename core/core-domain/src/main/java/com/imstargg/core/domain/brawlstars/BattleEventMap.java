package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.MessageCollection;
import jakarta.annotation.Nullable;

public record BattleEventMap(
        @Nullable MessageCollection names,
        @Nullable String imagePath
) {
}

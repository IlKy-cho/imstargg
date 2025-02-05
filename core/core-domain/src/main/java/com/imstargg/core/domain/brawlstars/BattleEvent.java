package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.enums.BattleEventMode;
import com.imstargg.core.enums.BattleMode;
import jakarta.annotation.Nullable;

import java.time.OffsetDateTime;

public record BattleEvent(
        BrawlStarsId id,
        BattleEventMode mode,
        BattleEventMap map,
        @Nullable BattleMode battleMode,
        @Nullable OffsetDateTime latestBattleTime
) {
}

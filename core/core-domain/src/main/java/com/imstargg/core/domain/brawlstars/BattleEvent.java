package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.enums.BattleEventMode;
import com.imstargg.core.enums.BattleMode;
import jakarta.annotation.Nullable;

public record BattleEvent(
        BrawlStarsId id,
        BattleEventMode mode,
        BattleEventMap map,
        @Nullable BattleMode battleMode
) {
}

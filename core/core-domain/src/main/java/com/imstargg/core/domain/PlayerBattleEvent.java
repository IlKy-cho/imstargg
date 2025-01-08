package com.imstargg.core.domain;

import com.imstargg.core.domain.brawlstars.BattleEventMap;
import com.imstargg.core.enums.BattleEventMode;
import jakarta.annotation.Nullable;

public record PlayerBattleEvent(
        @Nullable BrawlStarsId id,
        @Nullable BattleEventMode mode,
        BattleEventMap map
) {
}

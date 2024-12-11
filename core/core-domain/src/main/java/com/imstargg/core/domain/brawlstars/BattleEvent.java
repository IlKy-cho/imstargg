package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.enums.BattleEventMode;

public record BattleEvent(
        BrawlStarsId id,
        BattleEventMode mode,
        BattleMap map
) {
}

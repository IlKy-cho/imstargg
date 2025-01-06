package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.enums.BattleEventMode;

import java.time.LocalDateTime;

public record BattleEvent(
        BrawlStarsId id,
        BattleEventMode mode,
        BattleEventMap map,
        LocalDateTime latestBattleTime
) {
}

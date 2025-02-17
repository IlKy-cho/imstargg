package com.imstargg.admin.domain;

import com.imstargg.storage.db.core.BattleEntityEvent;

import java.time.LocalDateTime;

public record BattleEvent(
        BattleEntityEvent entity,
        BattleEventMap map,
        String battleMode,
        LocalDateTime latestBattleTime,
        boolean soloRanked
) {
}

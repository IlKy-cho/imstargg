package com.imstargg.admin.domain;

import com.imstargg.storage.db.core.brawlstars.BattleEventCollectionEntity;
import jakarta.annotation.Nullable;

import java.time.OffsetDateTime;

public record BattleEvent(
        BattleEventCollectionEntity entity,
        BattleEventMap map,
        @Nullable String battleMode,
        @Nullable OffsetDateTime latestBattleTime,
        boolean soloRanked
) {
}

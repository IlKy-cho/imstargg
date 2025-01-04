package com.imstargg.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.imstargg.storage.db.core.brawlstars.BattleEventCollectionEntity;
import jakarta.annotation.Nullable;

import java.time.LocalDateTime;

public record BattleEvent(
        @JsonIgnoreProperties("map")
        BattleEventCollectionEntity entity,
        BattleEventMap map,
        @Nullable LocalDateTime battleTime,
        boolean seasoned
) {
}

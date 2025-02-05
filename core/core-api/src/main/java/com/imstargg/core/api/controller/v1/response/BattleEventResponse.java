package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.brawlstars.BattleEvent;
import com.imstargg.core.enums.BattleEventMode;
import com.imstargg.core.enums.BattleMode;
import jakarta.annotation.Nullable;

public record BattleEventResponse(
        long id,
        BattleEventMode mode,
        @Nullable String mapName,
        @Nullable String mapImagePath,
        @Nullable BattleMode battleMode
) {

    public static BattleEventResponse from(BattleEvent event) {
        return new BattleEventResponse(
                event.id().value(),
                event.mode(),
                event.map().name(),
                event.map().imagePath(),
                event.battleMode()
        );
    }
}

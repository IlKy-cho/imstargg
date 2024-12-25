package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.brawlstars.BattleEvent;
import com.imstargg.core.enums.BattleEventMode;
import jakarta.annotation.Nullable;

public record BattleEventResponse(
        long id,
        BattleEventMode mode,
        String mapName,
        @Nullable String mapImageUrl
) {

    public static BattleEventResponse from(BattleEvent event) {
        return new BattleEventResponse(
                event.id().value(),
                event.mode(),
                event.map().name(),
                event.map().imageUrl()
        );
    }
}

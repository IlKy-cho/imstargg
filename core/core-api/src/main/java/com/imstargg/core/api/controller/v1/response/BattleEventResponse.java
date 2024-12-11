package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.brawlstars.BattleEvent;
import com.imstargg.core.enums.BattleEventMode;

public record BattleEventResponse(
        long id,
        BattleEventMode mode,
        String mapCode,
        String mapName
) {

    public static BattleEventResponse from(BattleEvent event) {
        return new BattleEventResponse(
                event.id().value(),
                event.mode(),
                event.map().code(),
                event.map().name()
        );
    }
}

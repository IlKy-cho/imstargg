package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.brawlstars.RotationBattleEvent;

import java.time.OffsetDateTime;

public record RotationBattleEventResponse(
        BattleEventResponse event,
        OffsetDateTime startTime,
        OffsetDateTime endTime
) {

    public static RotationBattleEventResponse of(
            RotationBattleEvent event
    ) {
        return new RotationBattleEventResponse(
                BattleEventResponse.from(event.event()),
                event.startTime(),
                event.endTime()
        );
    }
}

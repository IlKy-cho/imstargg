package com.imstargg.core.domain.brawlstars;

import java.time.OffsetDateTime;

public record RotationBattleEvent(
        BattleEvent event,
        OffsetDateTime startTime,
        OffsetDateTime endTime
) {
}

package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.brawlstars.BattleEvent;

public record BattleEventResultCount(
        BattleEvent event,
        ResultCount resultCount,
        StarPlayerCount starPlayerCount
) {

}

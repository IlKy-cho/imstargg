package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.brawlstars.BattleEvent;

public record BattleEventResultCount(
        BattleEvent event,
        ResultCount resultCount,
        StarPlayerCount starPlayerCount
) {

    public BattleEventResultCount merge(BattleEventResultCount other) {
        if (!event.id().equals(other.event.id())) {
            throw new IllegalArgumentException(
                    "Event ID is not matched. " + event.id() + " != " + other.event.id());
        }

        return new BattleEventResultCount(
                event,
                resultCount.merge(other.resultCount),
                starPlayerCount.merge(other.starPlayerCount)
        );
    }
}

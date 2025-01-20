package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;

public record BattleEventResultCount(
        BrawlStarsId eventId,
        ResultCount resultCount,
        StarPlayerCount starPlayerCount
) {

    public BattleEventResultCount merge(BattleEventResultCount other) {
        if (!eventId.equals(other.eventId)) {
            throw new IllegalArgumentException(
                    "Event ID is not matched. " + eventId + " != " + other.eventId);
        }

        return new BattleEventResultCount(
                eventId,
                resultCount.merge(other.resultCount),
                starPlayerCount.merge(other.starPlayerCount)
        );
    }
}

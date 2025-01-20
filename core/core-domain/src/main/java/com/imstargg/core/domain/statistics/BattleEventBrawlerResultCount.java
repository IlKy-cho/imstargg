package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;

public record BattleEventBrawlerResultCount(
        BrawlStarsId brawlerId,
        ResultCount resultCount,
        StarPlayerCount starPlayerCount
) {

    public BattleEventBrawlerResultCount merge(BattleEventBrawlerResultCount other) {
        if (!brawlerId.equals(other.brawlerId)) {
            throw new IllegalArgumentException("Brawler ID is not matched. " + brawlerId + " != " + other.brawlerId);
        }

        return new BattleEventBrawlerResultCount(
                brawlerId,
                resultCount.merge(other.resultCount),
                starPlayerCount.merge(other.starPlayerCount)
        );
    }
}

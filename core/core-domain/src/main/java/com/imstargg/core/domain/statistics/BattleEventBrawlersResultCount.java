package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;

import java.util.List;

public record BattleEventBrawlersResultCount(
        List<BrawlStarsId> brawlerIds,
        ResultCount resultCount
) {

    public BattleEventBrawlersResultCount merge(BattleEventBrawlersResultCount other) {
        if (!brawlerIds.equals(other.brawlerIds)) {
            throw new IllegalArgumentException(
                    "Brawler ID is not matched. " + brawlerIds + " != " + other.brawlerIds);
        }

        return new BattleEventBrawlersResultCount(
                brawlerIds,
                resultCount.merge(other.resultCount)
        );
    }
}

package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;

public record BrawlerResultCount(
        BrawlStarsId brawlerId,
        ResultCount resultCount,
        StarPlayerCount starPlayerCount
) {

    public BrawlerResultCount merge(BrawlerResultCount other) {
        if (!brawlerId.equals(other.brawlerId)) {
            throw new IllegalArgumentException("Brawler ID is not matched. " + brawlerId + " != " + other.brawlerId);
        }

        return new BrawlerResultCount(
                brawlerId,
                resultCount.merge(other.resultCount),
                starPlayerCount.merge(other.starPlayerCount)
        );
    }
}

package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;

import java.util.List;

public record BrawlersResultCount(
        List<BrawlStarsId> brawlerIds,
        ResultCount resultCount
) {

    public BrawlersResultCount merge(BrawlersResultCount other) {
        if (!brawlerIds.equals(other.brawlerIds)) {
            throw new IllegalArgumentException(
                    "Brawler ID is not matched. " + brawlerIds + " != " + other.brawlerIds);
        }

        return new BrawlersResultCount(
                brawlerIds,
                resultCount.merge(other.resultCount)
        );
    }
}

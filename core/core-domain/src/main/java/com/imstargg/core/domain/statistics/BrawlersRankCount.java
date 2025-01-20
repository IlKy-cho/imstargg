package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;

import java.util.List;

public record BrawlersRankCount(
        List<BrawlStarsId> brawlerIds,
        RankCount rankCount
) {

    public BrawlersRankCount merge(BrawlersRankCount other) {
        if (!brawlerIds.equals(other.brawlerIds)) {
            throw new IllegalArgumentException(
                    "Brawler ID is not matched. " + brawlerIds + " != " + other.brawlerIds);
        }

        return new BrawlersRankCount(
                brawlerIds,
                rankCount.merge(other.rankCount)
        );
    }

}

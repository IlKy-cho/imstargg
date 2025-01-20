package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;

public record BrawlerRankCount(
        BrawlStarsId brawlerId,
        RankCount rankCount
) {


    public BrawlerRankCount merge(BrawlerRankCount other) {
        if (!brawlerId.equals(other.brawlerId)) {
            throw new IllegalArgumentException(
                    "Brawler ID is not matched. " + brawlerId + " != " + other.brawlerId);
        }

        return new BrawlerRankCount(
                brawlerId,
                rankCount.merge(other.rankCount)
        );
    }

}

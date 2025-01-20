package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;

public record BattleEventBrawlerRankCount(
        BrawlStarsId brawlerId,
        RankCount rankCount
) {


    public BattleEventBrawlerRankCount merge(BattleEventBrawlerRankCount other) {
        if (!brawlerId.equals(other.brawlerId)) {
            throw new IllegalArgumentException(
                    "Brawler ID is not matched. " + brawlerId + " != " + other.brawlerId);
        }

        return new BattleEventBrawlerRankCount(
                brawlerId,
                rankCount.merge(other.rankCount)
        );
    }

}

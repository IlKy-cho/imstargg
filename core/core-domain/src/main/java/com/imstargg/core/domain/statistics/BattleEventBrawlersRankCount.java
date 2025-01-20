package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;

import java.util.List;

public record BattleEventBrawlersRankCount(
        List<BrawlStarsId> brawlerIds,
        RankCount rankCount
) {

    public BattleEventBrawlersRankCount merge(BattleEventBrawlersRankCount other) {
        if (!brawlerIds.equals(other.brawlerIds)) {
            throw new IllegalArgumentException(
                    "Brawler ID is not matched. " + brawlerIds + " != " + other.brawlerIds);
        }

        return new BattleEventBrawlersRankCount(
                brawlerIds,
                rankCount.merge(other.rankCount)
        );
    }

}

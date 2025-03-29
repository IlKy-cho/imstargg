package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;

public record BrawlerPairRankCount(
        BrawlStarsId brawlerId,
        BrawlStarsId otherBrawlerId,
        RankCount rankCount
) {

}

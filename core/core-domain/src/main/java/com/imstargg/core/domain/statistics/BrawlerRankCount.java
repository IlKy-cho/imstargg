package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;

public record BrawlerRankCount(
        BrawlStarsId brawlerId,
        RankCount rankCount
) {


}

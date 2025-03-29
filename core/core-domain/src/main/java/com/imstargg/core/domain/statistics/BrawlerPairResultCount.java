package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;

public record BrawlerPairResultCount(
        BrawlStarsId brawlerId,
        BrawlStarsId otherBrawlerId,
        ResultCount resultCount
) {
}

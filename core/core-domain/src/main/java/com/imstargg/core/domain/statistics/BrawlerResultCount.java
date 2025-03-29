package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;

public record BrawlerResultCount(
        BrawlStarsId brawlerId,
        ResultCount resultCount,
        StarPlayerCount starPlayerCount
) {

}

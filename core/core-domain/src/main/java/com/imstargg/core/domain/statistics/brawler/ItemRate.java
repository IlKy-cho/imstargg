package com.imstargg.core.domain.statistics.brawler;

import com.imstargg.core.domain.BrawlStarsId;

public record ItemRate(
        BrawlStarsId id,
        double value
) {
}

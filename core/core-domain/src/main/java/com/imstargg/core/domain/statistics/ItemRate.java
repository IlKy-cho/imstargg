package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;

public record ItemRate(
        BrawlStarsId id,
        double value
) {
}

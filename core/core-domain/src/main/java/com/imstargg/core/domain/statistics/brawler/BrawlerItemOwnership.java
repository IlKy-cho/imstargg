package com.imstargg.core.domain.statistics.brawler;

import java.util.List;

public record BrawlerItemOwnership(
        List<ItemRate> gadgets,
        List<ItemRate> starPowers,
        List<ItemRate> gears
) {
}

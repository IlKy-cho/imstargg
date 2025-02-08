package com.imstargg.core.domain.statistics.brawler;

import com.imstargg.core.domain.statistics.ItemRate;
import jakarta.annotation.Nullable;

import java.util.List;

public record BrawlerOwnershipRate(
        @Nullable ItemRate brawler,
        List<ItemRate> gadgets,
        List<ItemRate> starPowers,
        List<ItemRate> gears
) {
}

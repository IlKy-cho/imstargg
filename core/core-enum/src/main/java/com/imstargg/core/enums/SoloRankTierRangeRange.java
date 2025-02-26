package com.imstargg.core.enums;

import java.util.Arrays;
import java.util.List;

public enum SoloRankTierRangeRange {

    BRONZE_SILVER_GOLD_PLUS(
            SoloRankTierRange.BRONZE,
            SoloRankTierRange.SILVER,
            SoloRankTierRange.GOLD,
            SoloRankTierRange.DIAMOND,
            SoloRankTierRange.MYTHIC,
            SoloRankTierRange.LEGENDARY,
            SoloRankTierRange.MASTER,
            SoloRankTierRange.PRO
    ),
    DIAMOND_PLUS(
            SoloRankTierRange.DIAMOND,
            SoloRankTierRange.MYTHIC,
            SoloRankTierRange.LEGENDARY,
            SoloRankTierRange.MASTER,
            SoloRankTierRange.PRO
    ),
    MYTHIC_PLUS(
            SoloRankTierRange.MYTHIC,
            SoloRankTierRange.LEGENDARY,
            SoloRankTierRange.MASTER,
            SoloRankTierRange.PRO
    ),
    LEGENDARY_PLUS(
            SoloRankTierRange.LEGENDARY,
            SoloRankTierRange.MASTER,
            SoloRankTierRange.PRO
    ),
    MASTER_PLUS(
            SoloRankTierRange.MASTER,
            SoloRankTierRange.PRO
    ),
    ;

    private final List<SoloRankTierRange> ranges;

    SoloRankTierRangeRange(SoloRankTierRange... ranges) {
        this.ranges = Arrays.stream(ranges)
                .distinct()
                .toList();
    }

    public List<SoloRankTierRange> getRanges() {
        return ranges;
    }
}

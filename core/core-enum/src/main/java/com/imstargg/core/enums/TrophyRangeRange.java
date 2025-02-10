package com.imstargg.core.enums;

import java.util.Arrays;
import java.util.List;

public enum TrophyRangeRange {

    TROPHY_0_PLUS(
            TrophyRange.TROPHY_0_500,
            TrophyRange.TROPHY_501_1000,
            TrophyRange.TROPHY_1000_OVER
    ),
    TROPHY_500_PLUS(
            TrophyRange.TROPHY_501_1000,
            TrophyRange.TROPHY_1000_OVER
    ),
    TROPHY_1000_PLUS(
            TrophyRange.TROPHY_1000_OVER
    ),
    ;

    private final List<TrophyRange> ranges;

    TrophyRangeRange(TrophyRange... ranges) {
        this.ranges = Arrays.stream(ranges)
                .distinct()
                .toList();
    }

    public List<TrophyRange> getRanges() {
        return ranges;
    }
}

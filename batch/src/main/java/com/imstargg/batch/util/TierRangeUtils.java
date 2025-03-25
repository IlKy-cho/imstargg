package com.imstargg.batch.util;

import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;

import java.util.Arrays;
import java.util.Optional;

public abstract class TierRangeUtils {

    public static Optional<TrophyRange> findTrophyRange(String tierRange) {
        return Arrays.stream(TrophyRange.values())
                .filter(trophyRange -> trophyRange.name().equals(tierRange))
                .findFirst();
    }

    public static Optional<SoloRankTierRange> findSoloRankTierRange(String tierRange) {
        return Arrays.stream(SoloRankTierRange.values())
                .filter(soloRankTierRange -> soloRankTierRange.name().equals(tierRange))
                .findFirst();
    }

    private TierRangeUtils() {
        throw new UnsupportedOperationException();
    }
}

package com.imstargg.core.domain.statistics;

import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import jakarta.annotation.Nullable;

public record TierRange(
        @Nullable TrophyRange trophyRange,
        @Nullable SoloRankTierRange soloRankTierRange
) {

    @Nullable
    public String value() {
        if (trophyRange != null) {
            return trophyRange.name();
        } else if (soloRankTierRange != null) {
            return soloRankTierRange.name();
        }
        return null;
    }
}

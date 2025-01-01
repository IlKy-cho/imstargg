package com.imstargg.batch.util;

import com.imstargg.core.enums.BattleType;
import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import jakarta.annotation.Nullable;

public abstract class BattleUtils {

    @Nullable
    public static SoloRankTierRange soloRankTierRange(BattleType battleType, int value) {
        return BattleType.SOLO_RANKED == battleType
                ? SoloRankTierRange.of(value)
                : null;
    }

    @Nullable
    public static TrophyRange trophyRange(BattleType battleType, int value) {
        return BattleType.RANKED == battleType
                ? TrophyRange.of(value)
                : null;
    }

    private BattleUtils() {
    }
}

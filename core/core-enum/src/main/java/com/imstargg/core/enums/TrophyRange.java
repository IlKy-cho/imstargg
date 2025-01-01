package com.imstargg.core.enums;

import javax.annotation.Nullable;

public enum TrophyRange {

    TROPHY_0_500,
    TROPHY_501_1000,
    TROPHY_1001_1500,
    TROPHY_1501_2000,
    TROPHY_2000_OVER,
    ;

    public static TrophyRange of(int trophy) {
        if (trophy < 501) {
            return TROPHY_0_500;
        } else if (trophy < 1001) {
            return TROPHY_501_1000;
        } else if (trophy < 1501) {
            return TROPHY_1001_1500;
        } else if (trophy < 2001) {
            return TROPHY_1501_2000;
        } else {
            return TROPHY_2000_OVER;
        }
    }

    @Nullable
    public static TrophyRange of(BattleType battleType, int trophy) {
        return battleType == BattleType.RANKED
                ? TrophyRange.of(trophy)
                : null;
    }
}

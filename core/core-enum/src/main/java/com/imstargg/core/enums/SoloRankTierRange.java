package com.imstargg.core.enums;

import javax.annotation.Nullable;

public enum SoloRankTierRange {

    BRONZE,
    SILVER,
    GOLD,
    DIAMOND,
    MYTHIC,
    LEGENDARY,
    MASTER,
    PRO
    ;

    public static SoloRankTierRange of(SoloRankTier tier) {
        return switch (tier) {
            case BRONZE_1, BRONZE_2, BRONZE_3 -> BRONZE;
            case SILVER_1, SILVER_2, SILVER_3 -> SILVER;
            case GOLD_1, GOLD_2, GOLD_3 -> GOLD;
            case DIAMOND_1, DIAMOND_2, DIAMOND_3 -> DIAMOND;
            case MYTHIC_1, MYTHIC_2, MYTHIC_3 -> MYTHIC;
            case LEGENDARY_1, LEGENDARY_2, LEGENDARY_3 -> LEGENDARY;
            case MASTER_1, MASTER_2, MASTER_3 -> MASTER;
            case PRO -> PRO;
        };
    }

    public static SoloRankTierRange of(int value) {
        return SoloRankTierRange.of(SoloRankTier.of(value));
    }

    @Nullable
    public static SoloRankTierRange of(BattleType battleType, int value) {
        return battleType == BattleType.SOLO_RANKED
                ? SoloRankTierRange.of(value)
                : null;
    }
}

package com.imstargg.core.enums;

public enum SoloRankTierRange {

    BRONZE,
    SILVER,
    GOLD,
    DIAMOND,
    MYTHIC,
    LEGENDARY,
    MASTER,
    ;

    public static SoloRankTierRange of(SoloRankTier tier) {
        return switch (tier) {
            case BRONZE_1, BRONZE_2, BRONZE_3 -> BRONZE;
            case SILVER_1, SILVER_2, SILVER_3 -> SILVER;
            case GOLD_1, GOLD_2, GOLD_3 -> GOLD;
            case DIAMOND_1, DIAMOND_2, DIAMOND_3 -> DIAMOND;
            case MYTHIC_1, MYTHIC_2, MYTHIC_3 -> MYTHIC;
            case LEGENDARY_1, LEGENDARY_2, LEGENDARY_3 -> LEGENDARY;
            case MASTER -> MASTER;
        };
    }

    public static SoloRankTierRange of(int value) {
        return SoloRankTierRange.of(SoloRankTier.of(value));
    }
}

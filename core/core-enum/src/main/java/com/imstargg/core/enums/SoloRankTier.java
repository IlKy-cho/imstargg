package com.imstargg.core.enums;

public enum SoloRankTier {

    BRONZE_1,
    BRONZE_2,
    BRONZE_3,
    SILVER_1,
    SILVER_2,
    SILVER_3,
    GOLD_1,
    GOLD_2,
    GOLD_3,
    DIAMOND_1,
    DIAMOND_2,
    DIAMOND_3,
    MYTHIC_1,
    MYTHIC_2,
    MYTHIC_3,
    LEGENDARY_1,
    LEGENDARY_2,
    LEGENDARY_3,
    MASTER,
    ;

    public static SoloRankTier of(int value) {
        if (value < 1) {
            return BRONZE_1;
        } else if (value > 19) {
            return MASTER;
        }
        return switch (value) {
            case 1 -> BRONZE_1;
            case 2 -> BRONZE_2;
            case 3 -> BRONZE_3;
            case 4 -> SILVER_1;
            case 5 -> SILVER_2;
            case 6 -> SILVER_3;
            case 7 -> GOLD_1;
            case 8 -> GOLD_2;
            case 9 -> GOLD_3;
            case 10 -> DIAMOND_1;
            case 11 -> DIAMOND_2;
            case 12 -> DIAMOND_3;
            case 13 -> MYTHIC_1;
            case 14 -> MYTHIC_2;
            case 15 -> MYTHIC_3;
            case 16 -> LEGENDARY_1;
            case 17 -> LEGENDARY_2;
            case 18 -> LEGENDARY_3;
            case 19 -> MASTER;
            default -> throw new IllegalArgumentException("알 수 없는 티어입니다. value=" + value);
        };
    }
}

package com.imstargg.core.enums;

public enum SoloRankTierRange {

    BRONZE_SILVER_GOLD_PLUS(SoloRankTier.BRONZE_1),
    DIAMOND_PLUS(SoloRankTier.DIAMOND_1),
    MYTHIC_PLUS(SoloRankTier.MYTHIC_1),
    LEGENDARY_PLUS(SoloRankTier.LEGENDARY_1),
    MASTER_PLUS(SoloRankTier.MASTER_1),
    ;

    private final SoloRankTier minTier;

    SoloRankTierRange(SoloRankTier minTier) {
        this.minTier = minTier;
    }

    public SoloRankTier getMinTier() {
        return minTier;
    }
}

package com.imstargg.core.enums;

public enum TrophyRange {

    TROPHY_0_PLUS(0),
    TROPHY_500_PLUS(500),
    TROPHY_1000_PLUS(1000),
    ;

    private final int minTrophy;

    TrophyRange(int minTrophy) {
        this.minTrophy = minTrophy;
    }

    public int getMinTrophy() {
        return minTrophy;
    }
}

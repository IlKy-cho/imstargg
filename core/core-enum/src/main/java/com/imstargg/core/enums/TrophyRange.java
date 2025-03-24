package com.imstargg.core.enums;

import java.util.List;
import java.util.stream.Stream;

public enum TrophyRange {

    TROPHY_0_PLUS(0),
    TROPHY_500_PLUS(500),
    TROPHY_1000_PLUS(1000),
    ;

    private final int minTrophy;

    TrophyRange(int minTrophy) {
        this.minTrophy = minTrophy;
    }

    public static List<TrophyRange> findAll(int trophy) {
        return Stream.of(TrophyRange.values())
                .filter(trophyRange -> trophy >= trophyRange.getMinTrophy())
                .toList();
    }

    public int getMinTrophy() {
        return minTrophy;
    }
}

package com.imstargg.core.domain.statistics;

public record StarPlayerCount(
        long count
) {
    public double starPlayerRate(ResultCount resultCount) {
        long totalBattleCount = resultCount.totalBattleCount();
        if (totalBattleCount == 0) {
            return 0;
        }
        return (double) count / totalBattleCount;
    }

    public StarPlayerCount merge(StarPlayerCount other) {
        return new StarPlayerCount(
                count + other.count
        );
    }
}

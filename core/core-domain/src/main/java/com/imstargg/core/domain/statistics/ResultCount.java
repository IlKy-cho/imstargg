package com.imstargg.core.domain.statistics;

public record ResultCount(
        long victoryCount,
        long defeatCount,
        long drawCount
) {

    public long totalBattleCount() {
        return victoryCount + defeatCount + drawCount;
    }

    public double winRate() {
        long victoryDefeatCount = victoryCount + defeatCount;
        if (victoryDefeatCount == 0) {
            return 0;
        }
        return (double) victoryCount / victoryDefeatCount;
    }

    public double pickRate(long totalBattleCount) {
        if (totalBattleCount == 0) {
            return 0;
        }
        return (double) this.totalBattleCount() / totalBattleCount;
    }

    public ResultCount merge(ResultCount other) {
        return new ResultCount(
                victoryCount + other.victoryCount,
                defeatCount + other.defeatCount,
                drawCount + other.drawCount
        );
    }
}

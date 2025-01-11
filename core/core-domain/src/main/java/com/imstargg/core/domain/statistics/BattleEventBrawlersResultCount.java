package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;

import java.util.List;

public record BattleEventBrawlersResultCount(
        List<BrawlStarsId> brawlerIds,
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

    public double pickRate(long totalEventBattleCount) {
        if (totalEventBattleCount == 0) {
            return 0;
        }
        return (double) totalBattleCount() / totalEventBattleCount;
    }

    public BattleEventBrawlersResultCount merge(BattleEventBrawlersResultCount other) {
        if (!brawlerIds.equals(other.brawlerIds)) {
            throw new IllegalArgumentException(
                    "Brawler ID is not matched. " + brawlerIds + " != " + other.brawlerIds);
        }

        return new BattleEventBrawlersResultCount(
                brawlerIds,
                victoryCount + other.victoryCount,
                defeatCount + other.defeatCount,
                drawCount + other.drawCount
        );
    }
}

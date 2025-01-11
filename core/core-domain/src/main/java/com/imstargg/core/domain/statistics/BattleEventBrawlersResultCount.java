package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;

import java.util.List;

public record BattleEventBrawlersResultCount(
        List<BrawlStarsId> brawlerBrawlStarsIds,
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
        if (!brawlerBrawlStarsIds.equals(other.brawlerBrawlStarsIds)) {
            throw new IllegalArgumentException(
                    "Brawler ID is not matched. " + brawlerBrawlStarsIds + " != " + other.brawlerBrawlStarsIds);
        }

        return new BattleEventBrawlersResultCount(
                brawlerBrawlStarsIds,
                victoryCount + other.victoryCount,
                defeatCount + other.defeatCount,
                drawCount + other.drawCount
        );
    }
}

package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;

public record BattleEventBrawlerResultCount(
        BrawlStarsId brawlerId,
        long victoryCount,
        long defeatCount,
        long drawCount,
        long starPlayerCount
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

    public double starPlayerRate() {
        long totalBattleCount = totalBattleCount();
        if (totalBattleCount == 0) {
            return 0;
        }
        return (double) starPlayerCount / totalBattleCount;
    }

    public BattleEventBrawlerResultCount merge(BattleEventBrawlerResultCount other) {
        if (!brawlerId.equals(other.brawlerId)) {
            throw new IllegalArgumentException("Brawler ID is not matched. " + brawlerId + " != " + other.brawlerId);
        }

        return new BattleEventBrawlerResultCount(
                brawlerId,
                victoryCount + other.victoryCount,
                defeatCount + other.defeatCount,
                drawCount + other.drawCount,
                starPlayerCount + other.starPlayerCount
        );
    }
}

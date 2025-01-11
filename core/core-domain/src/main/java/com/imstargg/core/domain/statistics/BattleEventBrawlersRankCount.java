package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record BattleEventBrawlersRankCount(
        List<BrawlStarsId> brawlerIds,
        Map<Integer, Long> rankToCounts
) {

    public long totalBattleCount() {
        return rankToCounts.values().stream().mapToLong(Long::longValue).sum();
    }

    public double averageRank() {
        return rankToCounts.entrySet().stream()
                .mapToDouble(entry -> entry.getKey() * entry.getValue())
                .sum() / totalBattleCount();
    }

    public double pickRate(long totalBattleCount) {
        if (totalBattleCount == 0) {
            return 0;
        }

        return (double) totalBattleCount() / totalBattleCount;
    }

    public BattleEventBrawlersRankCount merge(BattleEventBrawlersRankCount other) {
        if (!brawlerIds.equals(other.brawlerIds)) {
            throw new IllegalArgumentException(
                    "Brawler ID is not matched. " + brawlerIds + " != " + other.brawlerIds);
        }

        return new BattleEventBrawlersRankCount(
                brawlerIds,
                mergeRankToCounts(rankToCounts, other.rankToCounts)
        );
    }

    private Map<Integer, Long> mergeRankToCounts(Map<Integer, Long> rankToCounts1, Map<Integer, Long> rankToCounts2) {
        Map<Integer, Long> mergedRankToCounts = new HashMap<>(rankToCounts1);
        rankToCounts2.forEach((rank, count) -> mergedRankToCounts.merge(rank, count, Long::sum));
        return mergedRankToCounts;
    }
}

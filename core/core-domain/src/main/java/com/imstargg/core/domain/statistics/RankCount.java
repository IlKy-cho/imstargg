package com.imstargg.core.domain.statistics;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public record RankCount(
        Map<Integer, Long> rankToCount
) {

    public long totalBattleCount() {
        return rankToCount.values().stream().mapToLong(Long::longValue).sum();
    }

    public double averageRank() {
        return rankToCount.entrySet().stream()
                .mapToDouble(entry -> entry.getKey() * entry.getValue())
                .sum() / totalBattleCount();
    }

    public double pickRate(long totalBattleCount) {
        if (totalBattleCount == 0) {
            return 0;
        }

        return (double) totalBattleCount() / totalBattleCount;
    }

    public RankCount merge(RankCount other) {
        Map<Integer, Long> mergedRankToCounts = new HashMap<>(rankToCount);
        other.rankToCount.forEach((rank, count) -> mergedRankToCounts.merge(rank, count, Long::sum));
        return new RankCount(
                Collections.unmodifiableMap(mergedRankToCounts)
        );
    }
}

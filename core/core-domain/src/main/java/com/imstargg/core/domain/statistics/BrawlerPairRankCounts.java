package com.imstargg.core.domain.statistics;

import java.util.List;

public record BrawlerPairRankCounts(
        List<BrawlerPairRankCount> counts
) {

    public static BrawlerPairRankCounts empty() {
        return new BrawlerPairRankCounts(List.of());
    }

    public long totalBattleCount() {
        return counts.stream()
                .map(BrawlerPairRankCount::rankCount)
                .mapToLong(RankCount::totalBattleCount)
                .sum();
    }

    public boolean isEmpty() {
        return counts.isEmpty();
    }

    public List<BrawlerPairRankStatistics> toStatistics() {
        long totalBattleCount = totalBattleCount();
        return counts.stream()
                .map(count -> new BrawlerPairRankStatistics(
                        count.brawlerId(),
                        count.otherBrawlerId(),
                        count.rankCount().totalBattleCount(),
                        count.rankCount().averageRank(),
                        count.rankCount().pickRate(totalBattleCount)
                )).toList();
    }
}

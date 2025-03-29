package com.imstargg.core.domain.statistics;

import java.util.List;

public record BrawlerRankCounts(
        List<BrawlerRankCount> counts
) {

    public static BrawlerRankCounts empty() {
        return new BrawlerRankCounts(List.of());
    }

    public long totalBattleCount() {
        return counts.stream()
                .map(BrawlerRankCount::rankCount)
                .mapToLong(RankCount::totalBattleCount)
                .sum();
    }

    public List<BrawlerRankStatistics> toStatistics() {
        long totalBattleCount = totalBattleCount();
        return counts.stream()
                .map(count -> new BrawlerRankStatistics(
                        count.brawlerId(),
                        count.rankCount().totalBattleCount(),
                        count.rankCount().averageRank(),
                        count.rankCount().pickRate(totalBattleCount)
                )).toList();
    }
}

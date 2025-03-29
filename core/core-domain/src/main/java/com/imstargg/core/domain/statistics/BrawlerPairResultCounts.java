package com.imstargg.core.domain.statistics;

import java.util.List;

public record BrawlerPairResultCounts(
        List<BrawlerPairResultCount> counts
) {

    public static BrawlerPairResultCounts empty() {
        return new BrawlerPairResultCounts(List.of());
    }

    public long totalBattleCount() {
        return counts.stream()
                .map(BrawlerPairResultCount::resultCount)
                .mapToLong(ResultCount::totalBattleCount)
                .sum();
    }

    public boolean isEmpty() {
        return counts.isEmpty();
    }

    public List<BrawlerPairResultStatistics> toStatistics() {
        long totalBattleCount = totalBattleCount();
        return counts.stream()
                .map(count -> new BrawlerPairResultStatistics(
                        count.brawlerId(),
                        count.otherBrawlerId(),
                        count.resultCount().totalBattleCount(),
                        count.resultCount().winRate(),
                        count.resultCount().pickRate(totalBattleCount)
                )).toList();
    }
}

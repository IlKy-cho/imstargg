package com.imstargg.core.domain.statistics;

import java.util.List;

public record BrawlerResultCounts(
        List<BrawlerResultCount> counts
) {

    public static BrawlerResultCounts empty() {
        return new BrawlerResultCounts(List.of());
    }

    public long totalBattleCount() {
        return counts.stream()
                .map(BrawlerResultCount::resultCount)
                .mapToLong(ResultCount::totalBattleCount)
                .sum();
    }

    public List<BrawlerResultStatistics> toStatistics() {
        long totalBattleCount = totalBattleCount();
        return counts.stream()
                .map(count -> new BrawlerResultStatistics(
                        count.brawlerId(),
                        count.resultCount().totalBattleCount(),
                        count.resultCount().winRate(),
                        count.resultCount().pickRate(totalBattleCount),
                        count.starPlayerCount().starPlayerRate(count.resultCount())
                )).toList();
    }
}

package com.imstargg.core.domain.statistics;

import java.util.List;

public record BattleEventResultCounts(
        List<BattleEventResultCount> counts
) {

    public static BattleEventResultCounts empty() {
        return new BattleEventResultCounts(List.of());
    }

    public long totalBattleCount() {
        return counts.stream()
                .map(BattleEventResultCount::resultCount)
                .mapToLong(ResultCount::totalBattleCount)
                .sum();
    }

    public List<BattleEventResultStatistics> toStatistics() {
        return counts.stream()
                .map(count -> new BattleEventResultStatistics(
                        count.event(),
                        count.resultCount().totalBattleCount(),
                        count.resultCount().winRate(),
                        count.starPlayerCount().starPlayerRate(count.resultCount())
                )).toList();
    }
}

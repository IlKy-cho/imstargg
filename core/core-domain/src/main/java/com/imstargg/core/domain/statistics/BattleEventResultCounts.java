package com.imstargg.core.domain.statistics;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public BattleEventResultCounts merge(BattleEventResultCounts other) {
        var eventIdToCount = new HashMap<>(counts.stream().collect(
                Collectors.toMap(BattleEventResultCount::event, Function.identity())
        ));
        other.counts.forEach(otherCount -> {
            BattleEventResultCount count = eventIdToCount.get(otherCount.event());
            if (count == null) {
                eventIdToCount.put(otherCount.event(), otherCount);
            } else {
                eventIdToCount.put(otherCount.event(), count.merge(otherCount));
            }
        });

        return new BattleEventResultCounts(eventIdToCount.values().stream().toList());
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

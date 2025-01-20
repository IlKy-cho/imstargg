package com.imstargg.core.domain.statistics;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public record BattleEventBrawlersResultCounts(
        List<BattleEventBrawlersResultCount> counts
) {

    public static BattleEventBrawlersResultCounts empty() {
        return new BattleEventBrawlersResultCounts(List.of());
    }

    public long totalBattleCount() {
        return counts.stream()
                .map(BattleEventBrawlersResultCount::resultCount)
                .mapToLong(ResultCount::totalBattleCount)
                .sum();
    }

    public boolean isEmpty() {
        return counts.isEmpty();
    }

    public List<BattleEventBrawlersResultStatistics> toStatistics() {
        long totalBattleCount = totalBattleCount();
        return counts.stream()
                .map(count -> new BattleEventBrawlersResultStatistics(
                        count.brawlerIds(),
                        count.resultCount().totalBattleCount(),
                        count.resultCount().winRate(),
                        count.resultCount().pickRate(totalBattleCount)
                )).toList();
    }

    public BattleEventBrawlersResultCounts merge(BattleEventBrawlersResultCounts other) {
        var brawlerIdToCount = new HashMap<>(counts.stream().collect(
                Collectors.toMap(BattleEventBrawlersResultCount::brawlerIds, Function.identity())
        ));
        other.counts.forEach(otherCount -> {
            BattleEventBrawlersResultCount count = brawlerIdToCount.get(otherCount.brawlerIds());
            if (count == null) {
                brawlerIdToCount.put(otherCount.brawlerIds(), otherCount);
            } else {
                brawlerIdToCount.put(otherCount.brawlerIds(), count.merge(otherCount));
            }
        });

        return new BattleEventBrawlersResultCounts(brawlerIdToCount.values().stream().toList());
    }
}

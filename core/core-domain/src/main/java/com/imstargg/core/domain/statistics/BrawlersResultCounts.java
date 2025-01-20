package com.imstargg.core.domain.statistics;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public record BrawlersResultCounts(
        List<BrawlersResultCount> counts
) {

    public static BrawlersResultCounts empty() {
        return new BrawlersResultCounts(List.of());
    }

    public long totalBattleCount() {
        return counts.stream()
                .map(BrawlersResultCount::resultCount)
                .mapToLong(ResultCount::totalBattleCount)
                .sum();
    }

    public boolean isEmpty() {
        return counts.isEmpty();
    }

    public List<BrawlersResultStatistics> toStatistics() {
        long totalBattleCount = totalBattleCount();
        return counts.stream()
                .map(count -> new BrawlersResultStatistics(
                        count.brawlerIds(),
                        count.resultCount().totalBattleCount(),
                        count.resultCount().winRate(),
                        count.resultCount().pickRate(totalBattleCount)
                )).toList();
    }

    public BrawlersResultCounts merge(BrawlersResultCounts other) {
        var brawlerIdToCount = new HashMap<>(counts.stream().collect(
                Collectors.toMap(BrawlersResultCount::brawlerIds, Function.identity())
        ));
        other.counts.forEach(otherCount -> {
            BrawlersResultCount count = brawlerIdToCount.get(otherCount.brawlerIds());
            if (count == null) {
                brawlerIdToCount.put(otherCount.brawlerIds(), otherCount);
            } else {
                brawlerIdToCount.put(otherCount.brawlerIds(), count.merge(otherCount));
            }
        });

        return new BrawlersResultCounts(brawlerIdToCount.values().stream().toList());
    }
}

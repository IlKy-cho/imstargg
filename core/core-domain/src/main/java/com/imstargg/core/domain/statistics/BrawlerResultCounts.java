package com.imstargg.core.domain.statistics;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public boolean isEmpty() {
        return counts.isEmpty();
    }

    public List<BrawlerResultStatistics> toStatistics() {
        long totalBattleCount = totalBattleCount();
        return counts.stream()
                .map(count -> new BrawlerResultStatistics(
                        count.brawlerId(),
                        count.resultCount().totalBattleCount(),
                        count.resultCount().winRate(),
                        count.resultCount().pickRate(totalBattleCount),
                        count.starPlayerCount().starPlayerRate(count.resultCount().totalBattleCount())
                )).toList();
    }

    public BrawlerResultCounts merge(BrawlerResultCounts other) {
        var brawlerIdToCount = new HashMap<>(counts.stream().collect(
                Collectors.toMap(BrawlerResultCount::brawlerId, Function.identity())
        ));
        other.counts.forEach(otherCount -> {
            BrawlerResultCount count = brawlerIdToCount.get(otherCount.brawlerId());
            if (count == null) {
                brawlerIdToCount.put(otherCount.brawlerId(), otherCount);
            } else {
                brawlerIdToCount.put(otherCount.brawlerId(), count.merge(otherCount));
            }
        });

        return new BrawlerResultCounts(brawlerIdToCount.values().stream().toList());
    }
}

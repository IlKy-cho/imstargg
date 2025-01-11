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
                .mapToLong(BattleEventBrawlersResultCount::totalBattleCount)
                .sum();
    }

    public boolean isEmpty() {
        return counts.isEmpty();
    }

    public List<BattleEventBrawlersResultStatistics> toStatistics() {
        long totalBattleCount = totalBattleCount();
        return counts.stream()
                .map(resultCount -> new BattleEventBrawlersResultStatistics(
                        resultCount.brawlerBrawlStarsIds(),
                        resultCount.totalBattleCount(),
                        resultCount.winRate(),
                        resultCount.pickRate(totalBattleCount)
                )).toList();
    }

    public BattleEventBrawlersResultCounts merge(BattleEventBrawlersResultCounts other) {
        var brawlerIdToCount = new HashMap<>(counts.stream().collect(
                Collectors.toMap(BattleEventBrawlersResultCount::brawlerBrawlStarsIds, Function.identity())
        ));
        other.counts.forEach(otherCount -> {
            BattleEventBrawlersResultCount count = brawlerIdToCount.get(otherCount.brawlerBrawlStarsIds());
            if (count == null) {
                brawlerIdToCount.put(otherCount.brawlerBrawlStarsIds(), otherCount);
            } else {
                brawlerIdToCount.put(otherCount.brawlerBrawlStarsIds(), count.merge(otherCount));
            }
        });

        return new BattleEventBrawlersResultCounts(brawlerIdToCount.values().stream().toList());
    }
}

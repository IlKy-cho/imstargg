package com.imstargg.core.domain.statistics;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public record BattleEventBrawlerResultCounts(
        List<BattleEventBrawlerResultCount> counts
) {

    public static BattleEventBrawlerResultCounts empty() {
        return new BattleEventBrawlerResultCounts(List.of());
    }

    public long totalBattleCount() {
        return counts.stream()
                .mapToLong(BattleEventBrawlerResultCount::totalBattleCount)
                .sum();
    }

    public boolean isEmpty() {
        return counts.isEmpty();
    }

    public List<BattleEventBrawlerResultStatistics> toStatistics() {
        long totalBattleCount = totalBattleCount();
        return counts.stream()
                .map(resultCount -> new BattleEventBrawlerResultStatistics(
                        resultCount.brawlerId(),
                        resultCount.totalBattleCount(),
                        resultCount.winRate(),
                        resultCount.pickRate(totalBattleCount),
                        resultCount.starPlayerRate()
                )).toList();
    }

    public BattleEventBrawlerResultCounts merge(BattleEventBrawlerResultCounts other) {
        var brawlerIdToCount = new HashMap<>(counts.stream().collect(
                Collectors.toMap(BattleEventBrawlerResultCount::brawlerId, Function.identity())
        ));
        other.counts.forEach(otherCount -> {
            BattleEventBrawlerResultCount count = brawlerIdToCount.get(otherCount.brawlerId());
            if (count == null) {
                brawlerIdToCount.put(otherCount.brawlerId(), otherCount);
            } else {
                brawlerIdToCount.put(otherCount.brawlerId(), count.merge(otherCount));
            }
        });

        return new BattleEventBrawlerResultCounts(brawlerIdToCount.values().stream().toList());
    }
}

package com.imstargg.core.domain.statistics;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public record BattleEventBrawlerRankCounts(
        List<BattleEventBrawlerRankCount> counts
) {

    public static BattleEventBrawlerRankCounts empty() {
        return new BattleEventBrawlerRankCounts(List.of());
    }

    public long totalBattleCount() {
        return counts.stream()
                .mapToLong(BattleEventBrawlerRankCount::totalBattleCount)
                .sum();
    }

    public boolean isEmpty() {
        return counts.isEmpty();
    }

    public BattleEventBrawlerRankCounts merge(BattleEventBrawlerRankCounts other) {
        var brawlerIdToCount = new HashMap<>(counts.stream().collect(
                Collectors.toMap(BattleEventBrawlerRankCount::brawlerId, Function.identity())
        ));

        other.counts.forEach(otherCount -> {
            BattleEventBrawlerRankCount count = brawlerIdToCount.get(otherCount.brawlerId());
            if (count == null) {
                brawlerIdToCount.put(otherCount.brawlerId(), otherCount);
            } else {
                brawlerIdToCount.put(otherCount.brawlerId(), count.merge(otherCount));
            }
        });

        return new BattleEventBrawlerRankCounts(brawlerIdToCount.values().stream().toList());
    }

    public List<BattleEventBrawlerRankStatistics> toStatistics() {
        long totalBattleCount = totalBattleCount();
        return counts.stream()
                .map(resultCount -> new BattleEventBrawlerRankStatistics(
                        resultCount.brawlerId(),
                        resultCount.totalBattleCount(),
                        resultCount.averageRank(),
                        resultCount.pickRate(totalBattleCount)
                )).toList();
    }
}

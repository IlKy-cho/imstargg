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
                .map(BattleEventBrawlerResultCount::resultCount)
                .mapToLong(ResultCount::totalBattleCount)
                .sum();
    }

    public boolean isEmpty() {
        return counts.isEmpty();
    }

    public List<BattleEventBrawlerResultStatistics> toStatistics() {
        long totalBattleCount = totalBattleCount();
        return counts.stream()
                .map(count -> new BattleEventBrawlerResultStatistics(
                        count.brawlerId(),
                        count.resultCount().totalBattleCount(),
                        count.resultCount().winRate(),
                        count.resultCount().pickRate(totalBattleCount),
                        count.starPlayerCount().starPlayerRate(count.resultCount().totalBattleCount())
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

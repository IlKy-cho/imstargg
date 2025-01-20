package com.imstargg.core.domain.statistics;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public record BattleEventBrawlersRankCounts(
        List<BattleEventBrawlersRankCount> counts
) {

    public static BattleEventBrawlersRankCounts empty() {
        return new BattleEventBrawlersRankCounts(List.of());
    }

    public long totalBattleCount() {
        return counts.stream()
                .map(BattleEventBrawlersRankCount::rankCount)
                .mapToLong(RankCount::totalBattleCount)
                .sum();
    }

    public boolean isEmpty() {
        return counts.isEmpty();
    }

    public BattleEventBrawlersRankCounts merge(BattleEventBrawlersRankCounts other) {
        var brawlerIdToCount = new HashMap<>(counts.stream().collect(
                Collectors.toMap(BattleEventBrawlersRankCount::brawlerIds, Function.identity())
        ));

        other.counts.forEach(otherCount -> {
            BattleEventBrawlersRankCount count = brawlerIdToCount.get(otherCount.brawlerIds());
            if (count == null) {
                brawlerIdToCount.put(otherCount.brawlerIds(), otherCount);
            } else {
                brawlerIdToCount.put(otherCount.brawlerIds(), count.merge(otherCount));
            }
        });

        return new BattleEventBrawlersRankCounts(brawlerIdToCount.values().stream().toList());
    }

    public List<BattleEventBrawlersRankStatistics> toStatistics() {
        long totalBattleCount = totalBattleCount();
        return counts.stream()
                .map(count -> new BattleEventBrawlersRankStatistics(
                        count.brawlerIds(),
                        count.rankCount().totalBattleCount(),
                        count.rankCount().averageRank(),
                        count.rankCount().pickRate(totalBattleCount)
                )).toList();
    }
}

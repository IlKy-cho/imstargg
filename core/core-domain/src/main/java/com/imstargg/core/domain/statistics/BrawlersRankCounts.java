package com.imstargg.core.domain.statistics;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public record BrawlersRankCounts(
        List<BrawlerPairRankCount> counts
) {

    public static BrawlersRankCounts empty() {
        return new BrawlersRankCounts(List.of());
    }

    public long totalBattleCount() {
        return counts.stream()
                .map(BrawlerPairRankCount::rankCount)
                .mapToLong(RankCount::totalBattleCount)
                .sum();
    }

    public boolean isEmpty() {
        return counts.isEmpty();
    }

    public BrawlersRankCounts merge(BrawlersRankCounts other) {
        var brawlerIdToCount = new HashMap<>(counts.stream().collect(
                Collectors.toMap(BrawlerPairRankCount::brawlerIds, Function.identity())
        ));

        other.counts.forEach(otherCount -> {
            BrawlerPairRankCount count = brawlerIdToCount.get(otherCount.brawlerIds());
            if (count == null) {
                brawlerIdToCount.put(otherCount.brawlerIds(), otherCount);
            } else {
                brawlerIdToCount.put(otherCount.brawlerIds(), count.merge(otherCount));
            }
        });

        return new BrawlersRankCounts(brawlerIdToCount.values().stream().toList());
    }

    public List<BrawlersRankStatistics> toStatistics() {
        long totalBattleCount = totalBattleCount();
        return counts.stream()
                .map(count -> new BrawlersRankStatistics(
                        count.brawlerIds(),
                        count.rankCount().totalBattleCount(),
                        count.rankCount().averageRank(),
                        count.rankCount().pickRate(totalBattleCount)
                )).toList();
    }
}

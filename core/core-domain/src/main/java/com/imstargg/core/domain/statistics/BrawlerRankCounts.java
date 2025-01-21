package com.imstargg.core.domain.statistics;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public record BrawlerRankCounts(
        List<BrawlerRankCount> counts
) {

    public static BrawlerRankCounts empty() {
        return new BrawlerRankCounts(List.of());
    }

    public long totalBattleCount() {
        return counts.stream()
                .map(BrawlerRankCount::rankCount)
                .mapToLong(RankCount::totalBattleCount)
                .sum();
    }

    public BrawlerRankCounts merge(BrawlerRankCounts other) {
        var brawlerIdToCount = new HashMap<>(counts.stream().collect(
                Collectors.toMap(BrawlerRankCount::brawlerId, Function.identity())
        ));

        other.counts.forEach(otherCount -> {
            BrawlerRankCount count = brawlerIdToCount.get(otherCount.brawlerId());
            if (count == null) {
                brawlerIdToCount.put(otherCount.brawlerId(), otherCount);
            } else {
                brawlerIdToCount.put(otherCount.brawlerId(), count.merge(otherCount));
            }
        });

        return new BrawlerRankCounts(brawlerIdToCount.values().stream().toList());
    }

    public List<BrawlerRankStatistics> toStatistics() {
        long totalBattleCount = totalBattleCount();
        return counts.stream()
                .map(count -> new BrawlerRankStatistics(
                        count.brawlerId(),
                        count.rankCount().totalBattleCount(),
                        count.rankCount().averageRank(),
                        count.rankCount().pickRate(totalBattleCount)
                )).toList();
    }
}

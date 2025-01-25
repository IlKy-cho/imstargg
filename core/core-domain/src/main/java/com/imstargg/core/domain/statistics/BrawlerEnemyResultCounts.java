package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public record BrawlerEnemyResultCounts(
        List<BrawlerEnemyResultCount> counts
) {

    public static BrawlerEnemyResultCounts empty() {
        return new BrawlerEnemyResultCounts(List.of());
    }

    public long totalBattleCount() {
        return counts.stream()
                .map(BrawlerEnemyResultCount::resultCount)
                .mapToLong(ResultCount::totalBattleCount)
                .sum();
    }

    public BrawlerEnemyResultCounts merge(BrawlerEnemyResultCounts other) {
        var brawlerIdEnemyIdToCount = counts.stream().collect(
                Collectors.toMap(count -> List.of(count.brawlerId(), count.enemyBrawlerId()), Function.identity())
        );

        other.counts.forEach(otherCount -> {
            List<BrawlStarsId> brawlerIdEnemyId = List.of(otherCount.brawlerId(), otherCount.enemyBrawlerId());
            BrawlerEnemyResultCount count = brawlerIdEnemyIdToCount.get(brawlerIdEnemyId);
            if (count == null) {
                brawlerIdEnemyIdToCount.put(brawlerIdEnemyId, otherCount);
            } else {
                brawlerIdEnemyIdToCount.put(brawlerIdEnemyId, count.merge(otherCount));
            }
        });

        return new BrawlerEnemyResultCounts(brawlerIdEnemyIdToCount.values().stream().toList());
    }

    public List<BrawlerEnemyResultStatistics> toStatistics() {
        long totalBattleCount = totalBattleCount();

        return counts.stream()
                .map(count -> new BrawlerEnemyResultStatistics(
                        count.brawlerId(),
                        count.enemyBrawlerId(),
                        count.resultCount().totalBattleCount(),
                        count.resultCount().winRate(),
                        count.resultCount().pickRate(totalBattleCount)
                )).toList();
    }
}

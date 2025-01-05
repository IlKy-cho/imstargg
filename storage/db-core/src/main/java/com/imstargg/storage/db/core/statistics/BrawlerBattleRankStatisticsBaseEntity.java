package com.imstargg.storage.db.core.statistics;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@MappedSuperclass
abstract class BrawlerBattleRankStatisticsBaseEntity extends BattleStatisticsBaseEntity {

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "rank_to_counts", columnDefinition = "json", updatable = false, nullable = false)
    private Map<Integer, Long> rankToCounts;

    protected BrawlerBattleRankStatisticsBaseEntity() {
    }

    public long getTotalBattleCount() {
        return rankToCounts.values().stream().mapToLong(Long::longValue).sum();
    }

    public long getRankWeightedSum() {
        return rankToCounts.entrySet().stream()
                .mapToLong(entry -> entry.getKey() * entry.getValue()).sum();
    }

    public double getAverageRank() {
        return (double) getRankWeightedSum() / getTotalBattleCount();
    }
}

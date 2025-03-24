package com.imstargg.storage.db.core.statistics;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@MappedSuperclass
abstract class BrawlerBattleRankStatisticsBaseEntity extends BattleStatisticsBaseEntity {

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "rank_to_counts", columnDefinition = "json", nullable = false, updatable = false)
    private Map<Integer, Long> rankToCounts;

    protected BrawlerBattleRankStatisticsBaseEntity() {
    }

    public Map<Integer, Long> getRankToCounts() {
        return rankToCounts;
    }
}

package com.imstargg.storage.db.core.statistics;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@MappedSuperclass
abstract class BrawlerBattleRankStatisticsBaseCollectionEntity extends BattleStatisticsBaseCollectionEntity {

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "rank_to_counts", columnDefinition = "json", nullable = false)
    private Map<Integer, Long> rankToCounts;

    protected BrawlerBattleRankStatisticsBaseCollectionEntity() {
    }

    protected BrawlerBattleRankStatisticsBaseCollectionEntity(
            long eventBrawlStarsId, LocalDate battleDate
    ) {
        super(eventBrawlStarsId, battleDate);
        rankToCounts = new HashMap<>();
    }

    public void countUp(int rank) {
        rankToCounts.put(rank, rankToCounts.getOrDefault(rank, 0L) + 1);
    }
}

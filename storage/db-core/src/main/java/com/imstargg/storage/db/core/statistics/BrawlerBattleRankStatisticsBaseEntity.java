package com.imstargg.storage.db.core.statistics;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
abstract class BrawlerBattleRankStatisticsBaseEntity extends BattleStatisticsBaseEntity {

    @Column(name = "rank_value", updatable = false, nullable = false)
    private int rank;

    @Column(name = "rank_count", nullable = false)
    private int count;

    protected BrawlerBattleRankStatisticsBaseEntity() {
    }

    public int getRank() {
        return rank;
    }

    public int getCount() {
        return count;
    }
}

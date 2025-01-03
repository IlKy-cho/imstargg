package com.imstargg.storage.db.core.statistics;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDate;

@MappedSuperclass
abstract class BrawlerBattleRankStatisticsBaseCollectionEntity extends BattleStatisticsBaseCollectionEntity {

    @Column(name = "rank_value", updatable = false, nullable = false)
    private int rank;

    @Column(name = "rank_count", nullable = false)
    private int count;

    protected BrawlerBattleRankStatisticsBaseCollectionEntity() {
    }

    protected BrawlerBattleRankStatisticsBaseCollectionEntity(
            long eventBrawlStarsId, LocalDate battleDate, int rank) {
        super(eventBrawlStarsId, battleDate);
        this.rank = rank;
    }

    public void countUp() {
        count++;
    }

    public int getRank() {
        return rank;
    }

    public int getCount() {
        return count;
    }
}

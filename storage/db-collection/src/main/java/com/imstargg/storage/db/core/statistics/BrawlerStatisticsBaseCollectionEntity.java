package com.imstargg.storage.db.core.statistics;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDate;

@MappedSuperclass
abstract class BrawlerStatisticsBaseCollectionEntity {

    @Column(name = "battle_event_id", updatable = false, nullable = false)
    private long battleEventId;

    @Column(name = "battle_date", updatable = false, nullable = false)
    private LocalDate battleDate;

    @Column(name = "brawler_brawlstars_id", updatable = false, nullable = false)
    private long brawlerBrawlStarsId;

    protected BrawlerStatisticsBaseCollectionEntity() {
    }

    protected BrawlerStatisticsBaseCollectionEntity(
            long battleEventId,
            LocalDate battleDate,
            long brawlerBrawlStarsId
    ) {
        this.battleEventId = battleEventId;
        this.battleDate = battleDate;
        this.brawlerBrawlStarsId = brawlerBrawlStarsId;
    }

    public long getBattleEventId() {
        return battleEventId;
    }

    public LocalDate getBattleDate() {
        return battleDate;
    }

    public long getBrawlerBrawlStarsId() {
        return brawlerBrawlStarsId;
    }
}

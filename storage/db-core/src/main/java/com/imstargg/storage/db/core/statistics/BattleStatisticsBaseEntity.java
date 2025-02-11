package com.imstargg.storage.db.core.statistics;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
abstract class BattleStatisticsBaseEntity {

    @Column(name = "season_number", updatable = false, nullable = false)
    private int seasonNumber;

    @Column(name = "event_brawlstars_id", updatable = false, nullable = false)
    private long eventBrawlStarsId;

    protected BattleStatisticsBaseEntity() {
    }

    public long getEventBrawlStarsId() {
        return eventBrawlStarsId;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }
}

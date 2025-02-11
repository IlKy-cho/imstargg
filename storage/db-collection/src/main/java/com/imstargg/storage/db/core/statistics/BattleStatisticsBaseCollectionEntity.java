package com.imstargg.storage.db.core.statistics;

import com.imstargg.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
abstract class BattleStatisticsBaseCollectionEntity extends BaseEntity {

    @Column(name = "season_number", updatable = false, nullable = false)
    private int seasonNumber;

    @Column(name = "event_brawlstars_id", updatable = false, nullable = false)
    private long eventBrawlStarsId;

    protected BattleStatisticsBaseCollectionEntity() {
    }

    protected BattleStatisticsBaseCollectionEntity(
            int seasonNumber,
            long eventBrawlStarsId
    ) {
        this.eventBrawlStarsId = eventBrawlStarsId;
        this.seasonNumber = seasonNumber;
    }

    public long getEventBrawlStarsId() {
        return eventBrawlStarsId;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

}

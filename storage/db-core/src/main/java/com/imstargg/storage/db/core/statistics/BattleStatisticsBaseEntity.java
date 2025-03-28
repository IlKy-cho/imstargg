package com.imstargg.storage.db.core.statistics;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDate;

@MappedSuperclass
abstract class BattleStatisticsBaseEntity {

    @Column(name = "event_brawlstars_id", updatable = false, nullable = false)
    private long eventBrawlStarsId;

    @Column(name = "battle_date", updatable = false, nullable = false)
    private LocalDate battleDate;

    protected BattleStatisticsBaseEntity() {
    }

    public long getEventBrawlStarsId() {
        return eventBrawlStarsId;
    }

    public LocalDate getBattleDate() {
        return battleDate;
    }
}

package com.imstargg.storage.db.core.statistics;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDate;

@MappedSuperclass
abstract class BattleStatisticsBaseEntity {

    @Column(name = "event_brawlstars_id", updatable = false, nullable = false)
    private long eventBrawlStarsId;

    @Column(name = "brawler_brawlstars_id", updatable = false, nullable = false)
    private long brawlerBrawlStarsId;

    @Column(name = "tier_range", length = 25, updatable = false)
    private String tierRange;

    @Column(name = "battle_date", updatable = false, nullable = false)
    private LocalDate battleDate;

    protected BattleStatisticsBaseEntity() {
    }

    public long getEventBrawlStarsId() {
        return eventBrawlStarsId;
    }

    public long getBrawlerBrawlStarsId() {
        return brawlerBrawlStarsId;
    }

    public String getTierRange() {
        return tierRange;
    }

    public LocalDate getBattleDate() {
        return battleDate;
    }
}

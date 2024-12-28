package com.imstargg.storage.db.core.statistics;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDate;

@MappedSuperclass
abstract class BrawlerStatisticsBaseEntity {

    @Column(name = "battle_event_id", updatable = false, nullable = false)
    private long battleEventId;

    @Column(name = "battle_date", updatable = false, nullable = false)
    private LocalDate battleDate;

    @Column(name = "brawler_brawlstars_id", updatable = false, nullable = false)
    private long brawlerBrawlStarsId;

    protected BrawlerStatisticsBaseEntity() {
    }
}

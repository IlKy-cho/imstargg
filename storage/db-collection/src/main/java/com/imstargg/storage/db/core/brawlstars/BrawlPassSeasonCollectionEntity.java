package com.imstargg.storage.db.core.brawlstars;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.DayOfWeek;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;

@Entity
@Table(name = "brawl_pass_season")
public class BrawlPassSeasonCollectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brawl_pass_season_id")
    private Long id;

    @Column(name = "number", updatable = false, nullable = false)
    private Integer number;

    @Column(name = "start_time", updatable = false, nullable = false)
    private OffsetDateTime startTime;

    @Column(name = "end_time", updatable = false, nullable = false)
    private OffsetDateTime endTime;

    protected BrawlPassSeasonCollectionEntity() {
    }

    public BrawlPassSeasonCollectionEntity(Integer number, OffsetDateTime startTime, OffsetDateTime endTime) {
        this.number = number;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public BrawlPassSeasonCollectionEntity next() {
        OffsetDateTime nextEndTime = endTime.plusMonths(1)
                .with(TemporalAdjusters.firstInMonth(DayOfWeek.THURSDAY))
                .withHour(18)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .withOffsetSameInstant(ZoneOffset.ofHours(9));
        return new BrawlPassSeasonCollectionEntity(number + 1, endTime, nextEndTime);
    }

    public Long getId() {
        return id;
    }

    public Integer getNumber() {
        return number;
    }

    public OffsetDateTime getStartTime() {
        return startTime;
    }

    public OffsetDateTime getEndTime() {
        return endTime;
    }
}

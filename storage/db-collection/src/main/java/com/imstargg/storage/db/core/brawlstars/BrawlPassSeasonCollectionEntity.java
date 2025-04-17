package com.imstargg.storage.db.core.brawlstars;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.DayOfWeek;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAdjusters;

@Entity
@Table(name = "brawl_pass_season")
public class BrawlPassSeasonCollectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brawl_pass_season_id")
    private Long id;

    @Column(name = "season_number", nullable = false, updatable = false)
    private int number;

    @Column(name = "start_at", nullable = false, updatable = false)
    private OffsetDateTime startAt;

    @Column(name = "end_at", nullable = false, updatable = false)
    private OffsetDateTime endAt;

    public static BrawlPassSeasonCollectionEntity create37Season() {
        return new BrawlPassSeasonCollectionEntity(
                37,
                OffsetDateTime.parse("2025-04-03T18:00:00+09:00"),
                OffsetDateTime.parse("2025-05-01T18:00:00+09:00")
        );
    }

    protected BrawlPassSeasonCollectionEntity() {
    }

    private BrawlPassSeasonCollectionEntity(
            int number,
            OffsetDateTime startAt,
            OffsetDateTime endAt
    ) {
        this.number = number;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public BrawlPassSeasonCollectionEntity next() {
        OffsetDateTime nextStartAt = this.endAt;
        OffsetDateTime nextEndAt = endAt
                .plusMonths(1)
                .withDayOfMonth(1)
                .with(TemporalAdjusters.dayOfWeekInMonth(1, DayOfWeek.THURSDAY));
        return new BrawlPassSeasonCollectionEntity(
                this.number + 1,
                nextStartAt,
                nextEndAt
        );
    }

    public Long getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public OffsetDateTime getStartAt() {
        return startAt;
    }

    public OffsetDateTime getEndAt() {
        return endAt;
    }
}

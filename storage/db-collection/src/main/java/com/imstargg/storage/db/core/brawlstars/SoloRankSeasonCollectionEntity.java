package com.imstargg.storage.db.core.brawlstars;

import com.imstargg.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;

@Entity
@Table(name = "solo_rank_season")
public class SoloRankSeasonCollectionEntity extends BaseEntity {

    private static final int SEASON_MAX_MONTH = 4;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "solo_rank_season_id")
    private Long id;

    @Column(name = "season_number", nullable = false, updatable = false)
    private int number;

    @Column(name = "season_month", nullable = false, updatable = false)
    private int month;

    @Column(name = "start_at", nullable = false, updatable = false)
    private OffsetDateTime startAt;

    @Column(name = "end_at", nullable = false, updatable = false)
    private OffsetDateTime endAt;

    public static SoloRankSeasonCollectionEntity createFirst() {
        ZoneOffset zoneOffset = ZoneOffset.ofHours(2);
        LocalTime time = LocalTime.of(11, 0);
        return new SoloRankSeasonCollectionEntity(
                1,
                1,
                OffsetDateTime.of(LocalDate.of(2025, 2, 25), time, zoneOffset),
                OffsetDateTime.of(LocalDate.of(2025, 4, 16), time, zoneOffset)
        );
    }

    protected SoloRankSeasonCollectionEntity() {
    }

    SoloRankSeasonCollectionEntity(
            int number,
            int month,
            OffsetDateTime startAt,
            OffsetDateTime endAt
    ) {
        this.number = number;
        this.month = month;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public SoloRankSeasonCollectionEntity next() {
        int nextMonth = month < SEASON_MAX_MONTH ? month + 1 : 1;
        int nextNumber = nextMonth == 1 ? number + 1 : number;
        OffsetDateTime nextMonthThirdThursday = endAt
                .plusMonths(1)
                .withDayOfMonth(1)
                .with(TemporalAdjusters.dayOfWeekInMonth(3, DayOfWeek.THURSDAY));
        OffsetDateTime nextStartAt = endAt.plusDays(1);
        OffsetDateTime nextEndAt = nextMonthThirdThursday.minusDays(1);
        return new SoloRankSeasonCollectionEntity(
                nextNumber,
                nextMonth,
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

    public int getMonth() {
        return month;
    }

    public OffsetDateTime getStartAt() {
        return startAt;
    }

    public OffsetDateTime getEndAt() {
        return endAt;
    }
}

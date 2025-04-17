package com.imstargg.storage.db.core.brawlstars;

import com.imstargg.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.OffsetDateTime;

@Entity
@Table(
        name = "solo_rank_season",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_solo_rank_season__key", columnNames = {"season_number", "season_month"})
        }
)
public class SoloRankSeasonEntity extends BaseEntity {

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

    protected SoloRankSeasonEntity() {
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

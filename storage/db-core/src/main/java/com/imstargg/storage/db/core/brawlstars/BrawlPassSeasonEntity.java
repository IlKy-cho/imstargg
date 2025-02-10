package com.imstargg.storage.db.core.brawlstars;

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
        name = "brawl_pass_season",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_brawl_pass_season__number", columnNames = {"number"})
        }
)
public class BrawlPassSeasonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brawl_pass_season_id")
    private Long id;

    @Column(name = "number", updatable = false, nullable = false)
    private Integer number;

    @Column(name = "start_time", updatable = false, nullable = false)
    private OffsetDateTime start_time;

    @Column(name = "end_time", updatable = false, nullable = false)
    private OffsetDateTime endTime;

    protected BrawlPassSeasonEntity() {
    }

    public Long getId() {
        return id;
    }

    public Integer getNumber() {
        return number;
    }

    public OffsetDateTime getStart_time() {
        return start_time;
    }

    public OffsetDateTime getEndTime() {
        return endTime;
    }
}

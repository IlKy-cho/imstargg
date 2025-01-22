package com.imstargg.storage.db.core.brawlstars;

import com.imstargg.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "brawler_gear",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_brawler_gear",
                        columnNames = "brawler_brawlstars_id, gear_brawlstars_id"
                )
        }
)
public class BrawlerGearEntity extends BaseEntity {

    @Id
    @Column(name = "brawler_gear_id")
    private Long id;

    @Column(name = "brawler_brawlstars_id", updatable = false, nullable = false)
    private long brawlerBrawlStarsId;

    @Column(name = "gear_brawlstars_id", updatable = false, nullable = false)
    private long gearBrawlStarsId;

    protected BrawlerGearEntity() {
    }

    public Long getId() {
        return id;
    }

    public long getBrawlerBrawlStarsId() {
        return brawlerBrawlStarsId;
    }

    public long getGearBrawlStarsId() {
        return gearBrawlStarsId;
    }
}
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
                        name = "uk_brawler_gear__brawlerid_gearid",
                        columnNames = "brawler_id, gear_id"
                )
        }
)
public class BrawlerGearEntity extends BaseEntity {

    @Id
    @Column(name = "brwaler_gear_id")
    private Long id;

    @Column(name = "brawler_id", updatable = false, nullable = false)
    private long brawlerId;

    @Column(name = "gear_id", updatable = false, nullable = false)
    private long gearId;

    protected BrawlerGearEntity() {
    }

    public Long getId() {
        return id;
    }

    public long getBrawlerId() {
        return brawlerId;
    }

    public long getGearId() {
        return gearId;
    }
}
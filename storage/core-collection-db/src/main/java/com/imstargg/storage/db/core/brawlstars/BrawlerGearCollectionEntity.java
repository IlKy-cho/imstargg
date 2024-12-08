package com.imstargg.storage.db.core.brawlstars;

import com.imstargg.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "brawler_gear")
public class BrawlerGearCollectionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brwaler_gear_id")
    private Long id;

    @Column(name = "brawler_id", updatable = false, nullable = false)
    private long brawlerId;

    @Column(name = "gear_id", updatable = false, nullable = false)
    private long gearId;

    protected BrawlerGearCollectionEntity() {
    }

    public BrawlerGearCollectionEntity(long brawlerId, long gearId) {
        this.brawlerId = brawlerId;
        this.gearId = gearId;
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
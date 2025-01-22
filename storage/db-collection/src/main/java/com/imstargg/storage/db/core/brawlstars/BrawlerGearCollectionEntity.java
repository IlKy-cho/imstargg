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
    @Column(name = "brawler_gear_id")
    private Long id;

    @Column(name = "brawler_brawlstars_id", updatable = false, nullable = false)
    private long brawlerBrawlStarsId;

    @Column(name = "gear_brawlstars_id", updatable = false, nullable = false)
    private long gearBrawlStarsId;

    protected BrawlerGearCollectionEntity() {
    }

    public BrawlerGearCollectionEntity(long brawlerBrawlStarsId, long gearBrawlStarsId) {
        this.brawlerBrawlStarsId = brawlerBrawlStarsId;
        this.gearBrawlStarsId = gearBrawlStarsId;
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
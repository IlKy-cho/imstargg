package com.imstargg.storage.db.core;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "star_power")
public class StarPowerCollectionEntity {


    @Id
    @Column(name = "star_power_id")
    private Long id;

    @Column(name = "brawl_stars_id", updatable = false, nullable = false)
    private long brawlStarsId;

    @Column(name = "name", length = 105, updatable = false, nullable = false)
    private String name;

    protected StarPowerCollectionEntity() {
    }

    public StarPowerCollectionEntity(long brawlStarsId, String name) {
        this.brawlStarsId = brawlStarsId;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public long getBrawlStarsId() {
        return brawlStarsId;
    }

    public String getName() {
        return name;
    }
}

package com.imstargg.storage.db.core;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "star_power",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_starpower__brawlstarsid",
                        columnNames = "brawl_stars_id"
                )
        }
)
public class StarPowerEntity {


    @Id
    @Column(name = "star_power_id")
    private Long id;

    @Column(name = "brawl_stars_id", updatable = false, nullable = false)
    private long brawlStarsId;

    @Column(name = "name", length = 105, updatable = false, nullable = false)
    private String name;

    protected StarPowerEntity() {
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

package com.imstargg.storage.db.core;

import com.imstargg.core.enums.GearRarity;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "gear",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_gear__brawlstarsid",
                        columnNames = "brawl_stars_id"
                )
        }
)
public class GearEntity extends BaseEntity {

    @Id
    @Column(name = "gear_id")
    private Long id;

    @Column(name = "brawl_stars_id", updatable = false, nullable = false)
    private long brawlStarsId;

    @Column(name = "name", length = 105, updatable = false, nullable = false)
    private String name;

    @Nullable
    @Enumerated(EnumType.STRING)
    @Column(name = "rarity", columnDefinition = "varchar(45)")
    private GearRarity rarity;

    protected GearEntity() {
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

    @Nullable
    public GearRarity getRarity() {
        return rarity;
    }
}

package com.imstargg.storage.db.core;

import com.imstargg.core.enums.GearRarity;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "gear")
public class GearCollectionEntity extends BaseEntity {

    @Id
    @Column(name = "gear_id")
    private Long id;

    @Column(name = "brawlstars_id", updatable = false, nullable = false)
    private long brawlStarsId;

    @Column(name = "name", length = 105, updatable = false, nullable = false)
    private String name;

    @Nullable
    @Enumerated(EnumType.STRING)
    @Column(name = "rarity", columnDefinition = "varchar(45)")
    private GearRarity rarity;

    protected GearCollectionEntity() {}

    public GearCollectionEntity(long brawlStarsId, String name) {
        this.brawlStarsId = brawlStarsId;
        this.name = name;
        this.rarity = null;
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

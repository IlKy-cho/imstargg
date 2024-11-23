package com.imstargg.storage.db.core;

import com.imstargg.core.enums.BrawlerRarity;
import com.imstargg.core.enums.BrawlerRole;
import com.imstargg.storage.db.support.LongSetToStringConverter;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "brwaler")
public class BrawlerCollectionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brawler_id")
    private Long id;

    @Column(name = "brawlstars_id", updatable = false, nullable = false)
    private long brawlStarsId;

    @Column(name = "name", length = 105, updatable = false, nullable = false)
    private String name;

    @Nullable
    @Enumerated(EnumType.STRING)
    @Column(name = "rarity", columnDefinition = "varchar(45)", updatable = false)
    private BrawlerRarity rarity;

    @Nullable
    @Enumerated(EnumType.STRING)
    @Column(name = "role", columnDefinition = "varchar(45)", updatable = false)
    private BrawlerRole role;

    @Convert(converter = LongSetToStringConverter.class)
    @Column(name = "gear_ids", columnDefinition = "varchar(255)", nullable = false)
    private Set<Long> gearIds = new HashSet<>();

    @Convert(converter = LongSetToStringConverter.class)
    @Column(name = "star_power_ids", columnDefinition = "varchar(255)", nullable = false)
    private Set<Long> starPowerIds = new HashSet<>();

    @Convert(converter = LongSetToStringConverter.class)
    @Column(name = "gadget_ids", columnDefinition = "varchar(255)", nullable = false)
    private Set<Long> gadgetIds = new HashSet<>();

    protected BrawlerCollectionEntity() {
    }

    public BrawlerCollectionEntity(
            long brawlStarsId,
            String name
    ) {
        this.brawlStarsId = brawlStarsId;
        this.name = name;
        this.rarity = null;
        this.role = null;
    }

    public boolean addGearId(long gearId) {
        return gearIds.add(gearId);
    }

    public boolean addStarPowerId(long starPowerId) {
        return starPowerIds.add(starPowerId);
    }

    public boolean addGadgetId(long gadgetId) {
        return gadgetIds.add(gadgetId);
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
    public BrawlerRarity getRarity() {
        return rarity;
    }

    @Nullable
    public BrawlerRole getRole() {
        return role;
    }
}

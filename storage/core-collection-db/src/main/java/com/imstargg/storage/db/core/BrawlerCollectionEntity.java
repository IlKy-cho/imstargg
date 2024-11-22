package com.imstargg.storage.db.core;

import com.imstargg.core.enums.BrawlerRarity;
import com.imstargg.core.enums.BrawlerRole;
import com.imstargg.storage.db.support.LongListToStringConverter;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "brwaler")
public class BrawlerCollectionEntity extends BaseEntity {

    @Id
    @Column(name = "brawler_id")
    private Long id;

    @Column(name = "brawl_stars_id", updatable = false, nullable = false)
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

    @Convert(converter = LongListToStringConverter.class)
    @Column(name = "gear_ids", columnDefinition = "varchar(255)", nullable = false)
    private List<Long> gearIds;

    @Convert(converter = LongListToStringConverter.class)
    @Column(name = "star_power_ids", columnDefinition = "varchar(255)", nullable = false)
    private List<Long> starPowerIds;

    @Convert(converter = LongListToStringConverter.class)
    @Column(name = "gadget_ids", columnDefinition = "varchar(255)", nullable = false)
    private List<Long> gadgetIds;

    protected BrawlerCollectionEntity() {
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

    public List<Long> getGearIds() {
        return gearIds;
    }

    public List<Long> getStarPowerIds() {
        return starPowerIds;
    }

    public List<Long> getGadgetIds() {
        return gadgetIds;
    }
}

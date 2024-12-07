package com.imstargg.storage.db.core.brawlstars;

import com.imstargg.core.enums.GearRarity;
import com.imstargg.storage.db.core.BaseEntity;
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

    @Column(name = "name_message", length = 105, updatable = false, nullable = false)
    private String nameMessageCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "rarity", columnDefinition = "varchar(45)", updatable = false, nullable = false)
    private GearRarity rarity;

    @Column(name = "brawler_id", updatable = false, nullable = false)
    private long brawlerId;

    protected GearCollectionEntity() {
    }

    public GearCollectionEntity(long brawlStarsId, GearRarity rarity, long brawlerId) {
        this.brawlStarsId = brawlStarsId;
        this.rarity = rarity;
        this.brawlerId = brawlerId;
        this.nameMessageCode = "gear." + brawlStarsId + ".name";
    }

    public Long getId() {
        return id;
    }

    public long getBrawlStarsId() {
        return brawlStarsId;
    }

    public String getNameMessageCode() {
        return nameMessageCode;
    }

    public GearRarity getRarity() {
        return rarity;
    }

    public long getBrawlerId() {
        return brawlerId;
    }
}
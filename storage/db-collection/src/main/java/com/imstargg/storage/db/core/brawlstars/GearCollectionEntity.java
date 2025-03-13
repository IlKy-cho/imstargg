package com.imstargg.storage.db.core.brawlstars;

import com.imstargg.core.enums.GearRarity;
import com.imstargg.storage.db.core.BaseEntity;
import com.imstargg.storage.db.core.MessageCodes;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "gear")
public class GearCollectionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gear_id")
    private Long id;

    @Column(name = "brawlstars_id", updatable = false, nullable = false)
    private long brawlStarsId;

    @Column(name = "name_message_code", length = 105, updatable = false, nullable = false)
    private String nameMessageCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "rarity", columnDefinition = "varchar(45)", updatable = false, nullable = false)
    private GearRarity rarity;

    protected GearCollectionEntity() {
    }

    public GearCollectionEntity(long brawlStarsId, GearRarity rarity) {
        this.brawlStarsId = brawlStarsId;
        this.rarity = rarity;
        this.nameMessageCode = MessageCodes.GEAR_NAME.code(brawlStarsId);
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

}
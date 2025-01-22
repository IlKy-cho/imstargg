package com.imstargg.storage.db.core.brawlstars;

import com.imstargg.core.enums.GearRarity;
import com.imstargg.storage.db.core.BaseEntity;
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
                        name = "uk_brawlstarsid",
                        columnNames = "brawlstars_id"
                )
        }
)
public class GearEntity extends BaseEntity {

    @Id
    @Column(name = "gear_id")
    private Long id;

    @Column(name = "brawlstars_id", updatable = false, nullable = false)
    private long brawlStarsId;

    @Column(name = "name_message_code", length = 105, updatable = false, nullable = false)
    private String nameMessageCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "rarity", columnDefinition = "varchar(45)", updatable = false, nullable = false)
    private GearRarity rarity;

    protected GearEntity() {
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
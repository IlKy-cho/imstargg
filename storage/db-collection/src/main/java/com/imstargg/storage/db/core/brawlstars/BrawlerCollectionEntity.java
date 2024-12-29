package com.imstargg.storage.db.core.brawlstars;

import com.imstargg.core.enums.BrawlerRarity;
import com.imstargg.core.enums.BrawlerRole;
import com.imstargg.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "brawler")
public class BrawlerCollectionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brawler_id")
    private Long id;

    @Column(name = "brawlstars_id", updatable = false, nullable = false)
    private long brawlStarsId;

    @Column(name = "name_message_code", length = 105, updatable = false, nullable = false)
    private String nameMessageCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "rarity", columnDefinition = "varchar(45)", updatable = false, nullable = false)
    private BrawlerRarity rarity;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", columnDefinition = "varchar(45)", updatable = false, nullable = false)
    private BrawlerRole role;

    protected BrawlerCollectionEntity() {
    }

    public BrawlerCollectionEntity(long brawlStarsId, BrawlerRarity rarity, BrawlerRole role) {
        this.brawlStarsId = brawlStarsId;
        this.rarity = rarity;
        this.role = role;
        this.nameMessageCode = "brawler." + brawlStarsId + ".name";
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

    public BrawlerRarity getRarity() {
        return rarity;
    }

    public BrawlerRole getRole() {
        return role;
    }
}
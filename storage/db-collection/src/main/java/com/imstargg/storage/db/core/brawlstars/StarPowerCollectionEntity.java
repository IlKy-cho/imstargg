package com.imstargg.storage.db.core.brawlstars;

import com.imstargg.storage.db.core.BaseEntity;
import com.imstargg.storage.db.core.MessageCodes;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "star_power")
public class StarPowerCollectionEntity extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "star_power_id")
    private Long id;

    @Column(name = "brawlstars_id", updatable = false, nullable = false)
    private long brawlStarsId;

    @Column(name = "name_message_code", length = 105, updatable = false, nullable = false)
    private String nameMessageCode;

    @Column(name = "brawler_brawlstars_id", updatable = false, nullable = false)
    private long brawlerBrawlStarsId;

    protected StarPowerCollectionEntity() {
    }

    public StarPowerCollectionEntity(long brawlStarsId, long brawlerBrawlStarsId) {
        this.brawlStarsId = brawlStarsId;
        this.brawlerBrawlStarsId = brawlerBrawlStarsId;
        this.nameMessageCode = MessageCodes.STAR_POWER_NAME.code(brawlStarsId);
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

    public long getBrawlerBrawlStarsId() {
        return brawlerBrawlStarsId;
    }
}
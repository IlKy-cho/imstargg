package com.imstargg.storage.db.core.brawlstars;

import com.imstargg.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "star_power")
public class StarPowerCollectionEntity extends BaseEntity {


    @Id
    @Column(name = "star_power_id")
    private Long id;

    @Column(name = "brawlstars_id", updatable = false, nullable = false)
    private long brawlStarsId;

    @Column(name = "name_message_code", length = 105, updatable = false, nullable = false)
    private String nameMessageCode;

    @Column(name = "brawler_id", updatable = false, nullable = false)
    private long brawlerId;

    protected StarPowerCollectionEntity() {
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
}
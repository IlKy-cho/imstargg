package com.imstargg.storage.db.core.brawlstars;

import com.imstargg.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "battle_map")
public class BattleMapCollectionEntity extends BaseEntity {

    @Id
    @Column(name = "battle_map_id")
    private Long id;

    @Column(name = "brawlstars_id", updatable = false, nullable = false)
    private long brawlStarsId;

    @Column(name = "name_message_code", length = 105, updatable = false, nullable = false)
    private String nameMessageCode;

    protected BattleMapCollectionEntity() {
    }

    public BattleMapCollectionEntity(long brawlStarsId) {
        this.brawlStarsId = brawlStarsId;
        this.nameMessageCode = "battle.map." + brawlStarsId + ".name";
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

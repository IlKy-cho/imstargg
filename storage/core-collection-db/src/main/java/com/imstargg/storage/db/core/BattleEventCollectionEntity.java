package com.imstargg.storage.db.core;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "battle_event")
public class BattleEventCollectionEntity extends BaseEntity {

    @Id
    @Column(name = "battle_event_id")
    private Long id;

    @Column(name = "brawlstars_id", updatable = false, nullable = false)
    private long brawlStarsId;

    @Column(name = "mode", length = 105, updatable = false, nullable = false)
    private String mode;

    @Column(name = "map", length = 105, updatable = false, nullable = false)
    private String map;

    protected BattleEventCollectionEntity() {
    }

    public BattleEventCollectionEntity(long brawlStarsId, String mode, String map) {
        this.brawlStarsId = brawlStarsId;
        this.mode = mode;
        this.map = map;
    }

    public Long getId() {
        return id;
    }

    public long getBrawlStarsId() {
        return brawlStarsId;
    }

    public String getMode() {
        return mode;
    }

    public String getMap() {
        return map;
    }
}

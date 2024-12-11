package com.imstargg.storage.db.core.brawlstars;

import com.imstargg.core.enums.BattleEventMode;
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
@Table(name = "battle_event")
public class BattleEventCollectionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "battle_event_id")
    private Long id;

    @Column(name = "brawlstars_id", updatable = false, nullable = false)
    private long brawlStarsId;

    @Enumerated(EnumType.STRING)
    @Column(name = "mode", length = 45, updatable = false, nullable = false)
    private BattleEventMode mode;

    @Column(name = "map_id", updatable = false, nullable = false)
    private long mapId;

    protected BattleEventCollectionEntity() {
    }

    public BattleEventCollectionEntity(long brawlStarsId, BattleEventMode mode, long mapId) {
        this.brawlStarsId = brawlStarsId;
        this.mode = mode;
        this.mapId = mapId;
    }

    public Long getId() {
        return id;
    }

    public long getBrawlStarsId() {
        return brawlStarsId;
    }

    public BattleEventMode getMode() {
        return mode;
    }

    public long getMapId() {
        return mapId;
    }
}

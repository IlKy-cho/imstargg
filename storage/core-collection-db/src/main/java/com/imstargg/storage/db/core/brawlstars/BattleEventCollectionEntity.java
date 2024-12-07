package com.imstargg.storage.db.core.brawlstars;

import com.imstargg.core.enums.BattleEventMode;
import com.imstargg.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Column(name = "battle_event_mode", length = 65, updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private BattleEventMode mode;

    @Column(name = "battle_map_id", updatable = false, nullable = false)
    private long battleMapId;

    protected BattleEventCollectionEntity() {
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

    public long getBattleMapId() {
        return battleMapId;
    }
}

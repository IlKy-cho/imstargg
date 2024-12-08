package com.imstargg.storage.db.core.brawlstars;

import com.imstargg.core.enums.BattleEventMode;
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
        name = "battle_event",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_battle_event__brawlstarsid",
                        columnNames = "brawlstars_id"
                )
        }
)
public class BattleEventEntity extends BaseEntity {

    @Id
    @Column(name = "battle_event_id")
    private Long id;

    @Column(name = "brawlstars_id", updatable = false, nullable = false)
    private long brawlStarsId;

    @Enumerated(EnumType.STRING)
    @Column(name = "mode", length = 45, updatable = false, nullable = false)
    private BattleEventMode mode;

    @Column(name = "map_id", updatable = false, nullable = false)
    private long mapId;

    protected BattleEventEntity() {
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

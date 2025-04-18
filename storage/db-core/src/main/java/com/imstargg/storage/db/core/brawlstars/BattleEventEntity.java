package com.imstargg.storage.db.core.brawlstars;

import com.imstargg.storage.db.core.BaseEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "battle_event",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_battle_event__brawlstarsid", columnNames = "brawlstars_id")
        }
)
public class BattleEventEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "battle_event_id")
    private Long id;

    @Column(name = "brawlstars_id", updatable = false, nullable = false)
    private long brawlStarsId;

    @Column(name = "mode", length = 45, updatable = false, nullable = false)
    private String mode;

    @Nullable
    @Column(name = "map_brawlstars_name", length = 105, updatable = false)
    private String mapBrawlStarsName;

    @Column(name = "solo_ranked", nullable = false, updatable = false)
    private boolean soloRanked;

    protected BattleEventEntity() {
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

    @Nullable
    public String getMapBrawlStarsName() {
        return mapBrawlStarsName;
    }

    public boolean isSoloRanked() {
        return soloRanked;
    }
}

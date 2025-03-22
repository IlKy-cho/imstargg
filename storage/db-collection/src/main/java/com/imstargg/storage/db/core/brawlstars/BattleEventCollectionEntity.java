package com.imstargg.storage.db.core.brawlstars;

import com.imstargg.core.enums.BattleEventMode;
import com.imstargg.storage.db.core.BaseEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "battle_event")
public class BattleEventCollectionEntity extends BaseEntity {

    @Id
    @Column(name = "battle_event_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "brawlstars_id", updatable = false, nullable = false)
    private long brawlStarsId;

    @Column(name = "mode", length = 45, nullable = false)
    private String mode;

    @Nullable
    @Column(name = "map_brawlstars_name", length = 105)
    private String mapBrawlStarsName;

    @Column(name = "solo_ranked", nullable = false)
    private boolean soloRanked;

    protected BattleEventCollectionEntity() {
    }

    public BattleEventCollectionEntity(
            long brawlStarsId,
            String mode,
            String mapBrawlStarsName
    ) {
        this.brawlStarsId = brawlStarsId;
        this.mode = mode;
        this.mapBrawlStarsName = mapBrawlStarsName;
    }

    public void update(
            String mode,
            @Nullable String mapBrawlStarsName
    ) {
        if (BattleEventMode.UNKNOWN.getCode().equals(mode)) {
            this.mode = mode;
        }
        if (mapBrawlStarsName != null) {
            this.mapBrawlStarsName = mapBrawlStarsName;
        }
    }

    public void updateSoloRanked(boolean soloRanked) {
        this.soloRanked = soloRanked;
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

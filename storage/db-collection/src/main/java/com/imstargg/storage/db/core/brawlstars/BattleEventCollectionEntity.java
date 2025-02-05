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

import java.time.OffsetDateTime;

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

    @Nullable
    @Column(name = "battle_mode", length = 45, nullable = false)
    private String battleMode;

    @Nullable
    @Column(name = "latest_battle_time")
    private OffsetDateTime latestBattleTime;

    protected BattleEventCollectionEntity() {
    }

    public BattleEventCollectionEntity(
            long brawlStarsId,
            String mode,
            String mapBrawlStarsName,
            @Nullable String battleMode,
            @Nullable OffsetDateTime latestBattleTime
    ) {
        this.brawlStarsId = brawlStarsId;
        this.mode = mode;
        this.mapBrawlStarsName = mapBrawlStarsName;
        this.battleMode = battleMode;
        this.latestBattleTime = latestBattleTime;
    }

    public void update(
            String mode,
            @Nullable String mapBrawlStarsName,
            @Nullable String battleMode,
            @Nullable OffsetDateTime latestBattleTime
    ) {
        if (BattleEventMode.UNKNOWN.getCode().equals(mode)) {
            this.mode = mode;
        }
        if (mapBrawlStarsName != null) {
            this.mapBrawlStarsName = mapBrawlStarsName;
        }
        if (battleMode != null) {
            this.battleMode = battleMode;
        }
        if (latestBattleTime != null
                && (this.latestBattleTime == null || latestBattleTime.isAfter(this.latestBattleTime))) {
            this.latestBattleTime = latestBattleTime;
        }
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

    @Nullable
    public String getBattleMode() {
        return battleMode;
    }

    @Nullable
    public OffsetDateTime getLatestBattleTime() {
        return latestBattleTime;
    }
}

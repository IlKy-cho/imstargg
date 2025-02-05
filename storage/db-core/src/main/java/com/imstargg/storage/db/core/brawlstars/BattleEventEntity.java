package com.imstargg.storage.db.core.brawlstars;

import com.imstargg.storage.db.core.BaseEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.OffsetDateTime;

@Entity
@Table(
        name = "battle_event",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_battle_event__brawlstarsid", columnNames = "brawlstars_id")
        }
)
public class BattleEventEntity extends BaseEntity {

    @Id
    @Column(name = "battle_event_id")
    private Long id;

    @Column(name = "brawlstars_id", updatable = false, nullable = false)
    private long brawlStarsId;

    @Column(name = "mode", length = 45, updatable = false, nullable = false)
    private String mode;

    @Nullable
    @Column(name = "map_brawlstars_name", length = 105, updatable = false)
    private String mapBrawlStarsName;

    @Nullable
    @Column(name = "battle_mode", length = 45, updatable = false)
    private String battleMode;

    @Nullable
    @Column(name = "latest_battle_time", updatable = false)
    private OffsetDateTime latestBattleTime;

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

    @Nullable
    public String getBattleMode() {
        return battleMode;
    }

    @Nullable
    public OffsetDateTime getLatestBattleTime() {
        return latestBattleTime;
    }
}

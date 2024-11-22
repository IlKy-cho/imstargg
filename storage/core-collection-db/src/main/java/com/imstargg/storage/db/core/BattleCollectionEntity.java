package com.imstargg.storage.db.core;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "battle")
public class BattleCollectionEntity extends BaseEntity {

    @Id
    @Column(name = "battle_id")
    private Long id;

    @Column(name = "battle_time", updatable = false, nullable = false)
    private LocalDateTime battleTime;

    @Column(name = "event_id", updatable = false, nullable = false)
    private long eventId;

    @Column(name = "mode", length = 105, updatable = false, nullable = false)
    private String mode;

    @Column(name = "type", length = 105, updatable = false, nullable = false)
    private String type;

    @Column(name = "duration", updatable = false, nullable = false)
    private int duration;

    @Nullable
    @Column(name = "star_player_id", updatable = false)
    private Long starPlayerId;

    protected BattleCollectionEntity() {
    }

    public BattleCollectionEntity(
            LocalDateTime battleTime,
            long eventId,
            String mode,
            String type,
            int duration,
            @Nullable Long starPlayerId
    ) {
        this.battleTime = battleTime;
        this.eventId = eventId;
        this.mode = mode;
        this.type = type;
        this.duration = duration;
        this.starPlayerId = starPlayerId;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getBattleTime() {
        return battleTime;
    }

    public long getEventId() {
        return eventId;
    }

    public String getMode() {
        return mode;
    }

    public String getType() {
        return type;
    }

    public int getDuration() {
        return duration;
    }

    @Nullable
    public Long getStarPlayerId() {
        return starPlayerId;
    }
}

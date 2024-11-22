package com.imstargg.storage.db.core;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "battle",
        indexes = {
                @Index(name = "ix_battle__eventid", columnList = "event_id"),
                @Index(name = "ix_battle__createdat", columnList = "created_at desc"),
        }
)
public class BattleEntity extends BaseEntity {

    @Id
    @Column(name = "battle_id")
    private Long id;

    @Column(name = "battle_time", updatable = false, nullable = false)
    private LocalDateTime battleTime;

    @Column(name = "event_id", updatable = false, nullable = false)
    private long eventId;

    @Column(name = "type", length = 105, updatable = false, nullable = false)
    private String type;

    @Column(name = "duration", updatable = false, nullable = false)
    private int duration;

    @Nullable
    @Column(name = "star_player_id", updatable = false)
    private Long starPlayerId;

    protected BattleEntity() {
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

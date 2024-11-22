package com.imstargg.storage.db.core;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "battle")
public class BattleEntity extends BaseEntity {

    @Id
    @Column(name = "battle_id")
    private Long id;

    @Column(name = "battle_time", updatable = false, nullable = false)
    private LocalDateTime battleTime;

    @Column(name = "event_id", updatable = false, nullable = false)
    private long eventId;

    @Column(name = "mode", length = 65, updatable = false, nullable = false)
    private String mode;

    @Column(name = "type", length = 65, updatable = false, nullable = false)
    private String type;

    @Column(name = "duration", updatable = false, nullable = false)
    private int duration;

    @Column(name = "star_player_id", updatable = false)
    private long starPlayerId;

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

    public String getMode() {
        return mode;
    }

    public String getType() {
        return type;
    }

    public int getDuration() {
        return duration;
    }

    public long getStarPlayerId() {
        return starPlayerId;
    }
}

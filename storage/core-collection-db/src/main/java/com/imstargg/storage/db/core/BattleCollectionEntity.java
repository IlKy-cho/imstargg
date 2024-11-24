package com.imstargg.storage.db.core;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "battle")
public class BattleCollectionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "battle_id")
    private Long id;

    @Column(name = "battle_key", updatable = false, nullable = false)
    private String battleKey;

    @Column(name = "battle_time", updatable = false, nullable = false)
    private LocalDateTime battleTime;

    @Column(name = "event_id", updatable = false, nullable = false)
    private long eventId;

    @Column(name = "mode", length = 105, updatable = false, nullable = false)
    private String mode;

    @Column(name = "type", length = 105, updatable = false, nullable = false)
    private String type;

    @Column(name = "result", length = 25, updatable = false, nullable = false)
    private String result;

    @Column(name = "duration", updatable = false, nullable = false)
    private int duration;

    @Nullable
    @Column(name = "star_player_id", updatable = false)
    private Long starPlayerId;

    @Embedded
    private BattleCollectionEntityPlayerEmbeddable player;

    protected BattleCollectionEntity() {
    }

    public BattleCollectionEntity(
            String battleKey,
            LocalDateTime battleTime,
            long eventId,
            String mode,
            String type,
            String result,
            int duration,
            @Nullable Long starPlayerId,
            BattleCollectionEntityPlayerEmbeddable player
    ) {
        this.battleKey = battleKey;
        this.battleTime = battleTime;
        this.eventId = eventId;
        this.mode = mode;
        this.type = type;
        this.result = result;
        this.duration = duration;
        this.starPlayerId = starPlayerId;
        this.player = player;
    }
}

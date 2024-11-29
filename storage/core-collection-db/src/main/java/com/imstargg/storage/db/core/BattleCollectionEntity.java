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
    @Column(name = "battle_id", nullable = false)
    private Long id;

    @Column(name = "battle_key", updatable = false, nullable = false)
    private String battleKey;

    @Column(name = "battle_time", updatable = false, nullable = false)
    private LocalDateTime battleTime;

    @Embedded
    private BattleCollectionEntityEvent event;

    @Column(name = "mode", length = 105, updatable = false, nullable = false)
    private String mode;

    @Column(name = "type", length = 105, updatable = false, nullable = false)
    private String type;

    @Column(name = "result", length = 25, updatable = false)
    private String result;

    @Column(name = "duration", updatable = false)
    private Integer duration;

    @Nullable
    @Column(name = "star_player_brawlstars_tag", length = 45, updatable = false)
    private String starPlayerBrawlStarsTag;

    @Embedded
    private BattleCollectionEntityPlayer player;

    protected BattleCollectionEntity() {
    }

    public BattleCollectionEntity(
            String battleKey,
            LocalDateTime battleTime,
            BattleCollectionEntityEvent event,
            String mode,
            String type,
            @Nullable String result,
            @Nullable Integer duration,
            @Nullable String starPlayerBrawlStarsTag,
            BattleCollectionEntityPlayer player
    ) {
        this.battleKey = battleKey;
        this.battleTime = battleTime;
        this.event = event;
        this.mode = mode;
        this.type = type;
        this.result = result;
        this.duration = duration;
        this.starPlayerBrawlStarsTag = starPlayerBrawlStarsTag;
        this.player = player;
    }

    public Long getId() {
        return id;
    }

    public String getBattleKey() {
        return battleKey;
    }

    public LocalDateTime getBattleTime() {
        return battleTime;
    }

    public BattleCollectionEntityEvent getEvent() {
        return event;
    }

    public String getMode() {
        return mode;
    }

    public String getType() {
        return type;
    }

    public String getResult() {
        return result;
    }

    public Integer getDuration() {
        return duration;
    }

    @Nullable
    public String getStarPlayerBrawlStarsTag() {
        return starPlayerBrawlStarsTag;
    }

    public BattleCollectionEntityPlayer getPlayer() {
        return player;
    }
}

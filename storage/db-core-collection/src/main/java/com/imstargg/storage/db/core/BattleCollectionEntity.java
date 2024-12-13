package com.imstargg.storage.db.core;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

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

    @Nullable
    @Column(name = "type", length = 105, updatable = false)
    private String type;

    @Nullable
    @Column(name = "result", length = 25, updatable = false)
    private String result;

    @Nullable
    @Column(name = "duration", updatable = false)
    private Integer duration;

    @Nullable
    @Column(name = "star_player_brawlstars_tag", length = 45, updatable = false)
    private String starPlayerBrawlStarsTag;

    @Embedded
    private BattleCollectionEntityPlayer player;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "teams", columnDefinition = "json", updatable = false, nullable = false)
    private List<List<BattleCollectionEntityTeamPlayer>> teams;

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
            BattleCollectionEntityPlayer player,
            List<List<BattleCollectionEntityTeamPlayer>> teams
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
        this.teams = teams;
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

    public List<List<BattleCollectionEntityTeamPlayer>> getTeams() {
        return teams;
    }

}

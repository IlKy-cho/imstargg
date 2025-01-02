package com.imstargg.storage.db.core;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(
        name = "battle",
        indexes = {
                @Index(name = "ix_battle__playerid_battletime", columnList = "player_id, battle_time desc"),
                @Index(name = "ix_battle__battletime", columnList = "battle_time desc"),
        }
)
public class BattleEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "battle_id", nullable = false)
    private Long id;

    @Column(name = "battle_key", updatable = false, nullable = false)
    private String battleKey;

    @Column(name = "battle_time", updatable = false, nullable = false)
    private LocalDateTime battleTime;

    @Embedded
    private BattleEntityEvent event;

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
    private BattleEntityPlayer player;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "teams", columnDefinition = "json", updatable = false, nullable = false)
    private List<List<BattleEntityTeamPlayer>> teams;

    protected BattleEntity() {
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getBattleTime() {
        return battleTime;
    }

    public BattleEntityEvent getEvent() {
        return event;
    }

    public String getMode() {
        return mode;
    }

    public String getType() {
        return type;
    }

    @Nullable
    public String getResult() {
        return result;
    }

    @Nullable
    public Integer getDuration() {
        return duration;
    }

    @Nullable
    public String getStarPlayerBrawlStarsTag() {
        return starPlayerBrawlStarsTag;
    }

    public BattleEntityPlayer getPlayer() {
        return player;
    }

    public List<List<BattleEntityTeamPlayer>> getTeams() {
        return teams;
    }
}

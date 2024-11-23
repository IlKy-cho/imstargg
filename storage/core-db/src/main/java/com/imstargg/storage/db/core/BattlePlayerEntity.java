package com.imstargg.storage.db.core;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "battle_player",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_battleplayer__battleid_playerid", columnNames = {"battle_id", "player_id"})
        },
        indexes = {
                @Index(name = "ix_battleplayer__battleid", columnList = "battle_id"),
                @Index(name = "ix_battleplayer__playerid", columnList = "player_id"),
                @Index(name = "ix_battleplayer__brawlerid", columnList = "brawler_id")
        }
)
public class BattlePlayerEntity extends BaseEntity {

    @Id
    @Column(name = "battle_player_id")
    private Long id;

    @Column(name = "battle_id", updatable = false, nullable = false)
    private long battleId;

    @Column(name = "player_id", updatable = false, nullable = false)
    private long playerId;

    @Column(name = "result", length = 25, updatable = false, nullable = false)
    private String result;

    @Column(name = "brawler_id", updatable = false, nullable = false)
    private long brawlerId;

    @Column(name = "power", updatable = false, nullable = false)
    private int power;

    @Nullable
    @Column(name = "trophies", updatable = false)
    private Integer trophies;

    protected BattlePlayerEntity() {
    }

    public Long getId() {
        return id;
    }

    public long getBattleId() {
        return battleId;
    }

    public long getPlayerId() {
        return playerId;
    }

    public String getResult() {
        return result;
    }

    public long getBrawlerId() {
        return brawlerId;
    }

    public int getPower() {
        return power;
    }

    @Nullable
    public Integer getTrophies() {
        return trophies;
    }

}

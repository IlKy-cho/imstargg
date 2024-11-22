package com.imstargg.storage.db.core;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "battle_player")
public class BattlePlayerEntity extends BaseEntity {

    @Id
    @Column(name = "battle_result_id")
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

    @Column(name = "trophies", updatable = false)
    private int trophies;

    @Column(name = "trophy_snapshot", updatable = false)
    private int trophySnapshot;

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

    public int getTrophies() {
        return trophies;
    }

    public int getTrophySnapshot() {
        return trophySnapshot;
    }
}

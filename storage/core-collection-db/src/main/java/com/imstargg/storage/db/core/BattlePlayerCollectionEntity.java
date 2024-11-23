package com.imstargg.storage.db.core;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "battle_player")
public class BattlePlayerCollectionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    // 트로피전에서만 저장
    @Nullable
    @Column(name = "trophies", updatable = false)
    private Integer trophies;

    // 경쟁전에서만 저장
    @Nullable
    @Column(name = "trophy_snapshot", updatable = false)
    private Integer trophySnapshot;

    protected BattlePlayerCollectionEntity() {
    }

    public BattlePlayerCollectionEntity(
            long battleId,
            long playerId,
            String result,
            long brawlerId,
            int power,
            @Nullable Integer trophies,
            @Nullable Integer trophySnapshot
    ) {
        this.battleId = battleId;
        this.playerId = playerId;
        this.result = result;
        this.brawlerId = brawlerId;
        this.power = power;
        this.trophies = trophies;
        this.trophySnapshot = trophySnapshot;
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

    @Nullable
    public Integer getTrophySnapshot() {
        return trophySnapshot;
    }
}

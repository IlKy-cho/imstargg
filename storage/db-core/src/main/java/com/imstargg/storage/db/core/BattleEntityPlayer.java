package com.imstargg.storage.db.core;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class BattleEntityPlayer {

    @Column(name = "player_id", updatable = false, nullable = false)
    private long playerId;

    @Nullable
    @Column(name = "battle_rank", updatable = false)
    private Integer rank;

    @Nullable
    @Column(name = "trophy_change", updatable = false)
    private Integer trophyChange;

    protected BattleEntityPlayer() {
    }

    public long getPlayerId() {
        return playerId;
    }

    @Nullable
    public Integer getRank() {
        return rank;
    }

    @Nullable
    public Integer getTrophyChange() {
        return trophyChange;
    }

}

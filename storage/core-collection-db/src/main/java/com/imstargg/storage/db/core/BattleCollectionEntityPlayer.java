package com.imstargg.storage.db.core;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class BattleCollectionEntityPlayer {

    @Column(name = "player_id", updatable = false, nullable = false)
    private long playerId;

    @Nullable
    @Column(name = "rank", updatable = false)
    private Integer rank;

    @Nullable
    @Column(name = "trophy_change", updatable = false)
    private Integer trophyChange;

    @Nullable
    @Column(name = "trophy_snapshot", updatable = false)
    private Integer trophySnapshot;

    protected BattleCollectionEntityPlayer() {
    }

    public BattleCollectionEntityPlayer(
            long playerId,
            @Nullable Integer rank,
            @Nullable Integer trophyChange,
            @Nullable Integer trophySnapshot
    ) {
        this.playerId = playerId;
        this.rank = rank;
        this.trophyChange = trophyChange;
        this.trophySnapshot = trophySnapshot;
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

    @Nullable
    public Integer getTrophySnapshot() {
        return trophySnapshot;
    }

}

package com.imstargg.storage.db.core;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Embeddable
public class BattleCollectionEntityPlayer {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player_id", updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private PlayerCollectionEntity player;

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
            PlayerCollectionEntity player,
            @Nullable Integer rank,
            @Nullable Integer trophyChange,
            @Nullable Integer trophySnapshot
    ) {
        this.player = player;
        this.rank = rank;
        this.trophyChange = trophyChange;
        this.trophySnapshot = trophySnapshot;
    }

    public PlayerCollectionEntity getPlayer() {
        return player;
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

package com.imstargg.storage.db.core;

import com.querydsl.core.annotations.QueryProjection;
import jakarta.annotation.Nullable;

public class PlayerLastBattleProjection {

    private PlayerCollectionEntity player;

    @Nullable
    private BattleCollectionEntity lastBattle;

    @QueryProjection
    public PlayerLastBattleProjection(PlayerCollectionEntity player, @Nullable BattleCollectionEntity lastBattle) {
        this.player = player;
        this.lastBattle = lastBattle;
    }

    public PlayerCollectionEntity getPlayer() {
        return player;
    }

    public BattleCollectionEntity getLastBattle() {
        return lastBattle;
    }
}

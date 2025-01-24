package com.imstargg.core.domain;

import com.imstargg.core.domain.brawlstars.BattleEventMap;
import com.imstargg.core.domain.brawlstars.BattleEventMapFixture;
import com.imstargg.core.enums.BattleEventMode;

import javax.annotation.Nullable;

public class PlayerBattleEventFixture {

    @Nullable
    private BrawlStarsId id = null;
    
    @Nullable
    private BattleEventMode mode = null;
    
    private BattleEventMap map = new BattleEventMapFixture().build();

    public PlayerBattleEventFixture id(@Nullable BrawlStarsId id) {
        this.id = id;
        return this;
    }

    public PlayerBattleEventFixture mode(@Nullable BattleEventMode mode) {
        this.mode = mode;
        return this;
    }

    public PlayerBattleEventFixture map(BattleEventMap map) {
        this.map = map;
        return this;
    }

    public PlayerBattleEvent build() {
        return new PlayerBattleEvent(id, mode, map);
    }
} 
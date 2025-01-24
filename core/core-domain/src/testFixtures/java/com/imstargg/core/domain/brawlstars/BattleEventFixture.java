package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.enums.BattleEventMode;
import com.imstargg.test.java.IntegerIncrementUtil;
import com.imstargg.test.java.LongIncrementUtil;

import java.time.LocalDateTime;

public class BattleEventFixture {
    private BrawlStarsId id = new BrawlStarsId(LongIncrementUtil.next());
    private BattleEventMode mode = BattleEventMode.values()[IntegerIncrementUtil.next(BattleEventMode.values().length)];
    private BattleEventMap map = new BattleEventMapFixture().build();
    private LocalDateTime latestBattleTime = LocalDateTime.now();

    public BattleEventFixture id(BrawlStarsId id) {
        this.id = id;
        return this;
    }

    public BattleEventFixture mode(BattleEventMode mode) {
        this.mode = mode;
        return this;
    }

    public BattleEventFixture map(BattleEventMap map) {
        this.map = map;
        return this;
    }

    public BattleEventFixture latestBattleTime(LocalDateTime latestBattleTime) {
        this.latestBattleTime = latestBattleTime;
        return this;
    }

    public BattleEvent build() {
        return new BattleEvent(id, mode, map, latestBattleTime);
    }
} 
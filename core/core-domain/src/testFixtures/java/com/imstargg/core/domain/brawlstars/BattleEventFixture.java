package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.enums.BattleEventMode;
import com.imstargg.core.enums.BattleMode;
import com.imstargg.test.java.IntegerIncrementUtil;
import com.imstargg.test.java.LongIncrementUtil;

public class BattleEventFixture {
    private BrawlStarsId id = new BrawlStarsId(LongIncrementUtil.next());
    private BattleEventMode mode = BattleEventMode.values()[IntegerIncrementUtil.next(BattleEventMode.values().length)];
    private BattleEventMap map = new BattleEventMapFixture().build();
    private final BattleMode battleMode = BattleMode.values()[IntegerIncrementUtil.next(BattleMode.values().length)];

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

    public BattleEvent build() {
        return new BattleEvent(id, mode, map, battleMode);
    }
} 
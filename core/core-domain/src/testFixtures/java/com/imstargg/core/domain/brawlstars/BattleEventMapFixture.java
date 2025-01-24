package com.imstargg.core.domain.brawlstars;


import com.imstargg.test.java.LongIncrementUtil;

import javax.annotation.Nullable;

public class BattleEventMapFixture {

    @Nullable
    private String name = "Map" + LongIncrementUtil.next();
    
    @Nullable
    private String imagePath = "/images/maps/" + LongIncrementUtil.next();

    public BattleEventMapFixture name(@Nullable String name) {
        this.name = name;
        return this;
    }

    public BattleEventMapFixture imagePath(@Nullable String imagePath) {
        this.imagePath = imagePath;
        return this;
    }

    public BattleEventMap build() {
        return new BattleEventMap(name, imagePath);
    }
} 
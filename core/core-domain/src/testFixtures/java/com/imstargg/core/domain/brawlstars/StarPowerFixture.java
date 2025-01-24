package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.test.java.LongIncrementUtil;

public class StarPowerFixture {
    private BrawlStarsId id = new BrawlStarsId(LongIncrementUtil.next());
    private String name = "StarPower-" + LongIncrementUtil.next();

    public StarPowerFixture id(BrawlStarsId id) {
        this.id = id;
        return this;
    }

    public StarPowerFixture name(String name) {
        this.name = name;
        return this;
    }

    public StarPower build() {
        return new StarPower(id, name);
    }
}
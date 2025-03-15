package com.imstargg.client.brawlstars.response;

import com.imstargg.test.java.IntegerIncrementUtil;
import com.imstargg.test.java.LongIncrementUtil;

public class GearStatResponseFixture {

    private long id = LongIncrementUtil.next();
    private String name = "gearName" + LongIncrementUtil.next();
    private int level = IntegerIncrementUtil.next();

    public GearStatResponseFixture id(long id) {
        this.id = id;
        return this;
    }

    public GearStatResponseFixture name(String name) {
        this.name = name;
        return this;
    }

    public GearStatResponseFixture level(int level) {
        this.level = level;
        return this;
    }

    public GearStatResponse build() {
        return new GearStatResponse(id, name, level);
    }
}

package com.imstargg.client.brawlstars.response;

import com.imstargg.test.java.LongIncrementUtil;

public class StarPowerResponseFixture {

    private long id = LongIncrementUtil.next();
    private String name = "starPowerName" + LongIncrementUtil.next();

    public StarPowerResponseFixture id(long id) {
        this.id = id;
        return this;
    }

    public StarPowerResponseFixture name(String name) {
        this.name = name;
        return this;
    }

    public StarPowerResponse build() {
        return new StarPowerResponse(id, name);
    }
}

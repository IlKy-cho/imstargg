package com.imstargg.client.brawlstars.response;

import com.imstargg.test.java.LongIncrementUtil;

public class AccessoryResponseFixture {

    private long id = LongIncrementUtil.next();
    private String name = "gadgetName" + LongIncrementUtil.next();

    public AccessoryResponseFixture id(long id) {
        this.id = id;
        return this;
    }

    public AccessoryResponseFixture name(String name) {
        this.name = name;
        return this;
    }

    public AccessoryResponse build() {
        return new AccessoryResponse(id, name);
    }
}

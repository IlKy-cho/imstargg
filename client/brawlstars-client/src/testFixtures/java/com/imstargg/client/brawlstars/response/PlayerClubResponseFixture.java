package com.imstargg.client.brawlstars.response;

import com.imstargg.test.java.LongIncrementUtil;

public class PlayerClubResponseFixture {
    private String tag = "#CLUBTAG" + LongIncrementUtil.next();
    private String name = "clubName" + LongIncrementUtil.next();

    public PlayerClubResponseFixture tag(String tag) {
        this.tag = tag;
        return this;
    }

    public PlayerClubResponseFixture name(String name) {
        this.name = name;
        return this;
    }

    public PlayerClubResponse build() {
        return new PlayerClubResponse(tag, name);
    }
}

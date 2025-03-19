package com.imstargg.client.brawlstars.response;

import com.imstargg.core.enums.BattleEventMode;
import com.imstargg.test.java.IntegerIncrementUtil;
import com.imstargg.test.java.LongIncrementUtil;

public class EventResponseFixture {
    private long id = LongIncrementUtil.next();
    private BattleEventMode mode = IntegerIncrementUtil.next(BattleEventMode.values());
    private String map = "map-" + id;

    public EventResponseFixture id(long id) {
        this.id = id;
        return this;
    }

    public EventResponseFixture mode(BattleEventMode mode) {
        this.mode = mode;
        return this;
    }

    public EventResponseFixture map(String map) {
        this.map = map;
        return this;
    }

    public EventResponse build() {
        return new EventResponse(id, mode.getCode(), map);
    }
}

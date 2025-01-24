package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.test.java.LongIncrementUtil;

public class GadgetFixture {
    private BrawlStarsId id = new BrawlStarsId(LongIncrementUtil.next());
    private String name = "Gadget-" + LongIncrementUtil.next();

    public GadgetFixture id(BrawlStarsId id) {
        this.id = id;
        return this;
    }

    public GadgetFixture name(String name) {
        this.name = name;
        return this;
    }

    public Gadget build() {
        return new Gadget(id, name);
    }
}
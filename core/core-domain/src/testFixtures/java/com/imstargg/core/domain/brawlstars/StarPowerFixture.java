package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.test.java.LongIncrementUtil;

import javax.annotation.Nullable;

public class StarPowerFixture {
    private BrawlStarsId id = new BrawlStarsId(LongIncrementUtil.next());
    private String name = "StarPower-" + LongIncrementUtil.next();
    @Nullable
    private String imagePath = "imagePath-" + LongIncrementUtil.next();

    public StarPowerFixture id(BrawlStarsId id) {
        this.id = id;
        return this;
    }

    public StarPowerFixture name(String name) {
        this.name = name;
        return this;
    }

    public StarPowerFixture imagePath(@Nullable String imagePath) {
        this.imagePath = imagePath;
        return this;
    }

    public StarPower build() {
        return new StarPower(id, name, imagePath);
    }
}
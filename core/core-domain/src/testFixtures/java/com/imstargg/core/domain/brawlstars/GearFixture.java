package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.enums.GearRarity;
import com.imstargg.test.java.IntegerIncrementUtil;
import com.imstargg.test.java.LongIncrementUtil;

import javax.annotation.Nullable;

public class GearFixture {
    private BrawlStarsId id = new BrawlStarsId(LongIncrementUtil.next());
    private GearRarity rarity = GearRarity.values()[IntegerIncrementUtil.next(GearRarity.values().length)];
    private String name = "Gear-" + LongIncrementUtil.next();
    @Nullable
    private String imagePath = "imagePath-" + LongIncrementUtil.next();

    public GearFixture id(BrawlStarsId id) {
        this.id = id;
        return this;
    }

    public GearFixture rarity(GearRarity rarity) {
        this.rarity = rarity;
        return this;
    }

    public GearFixture name(String name) {
        this.name = name;
        return this;
    }

    public GearFixture imagePath(@Nullable String imagePath) {
        this.imagePath = imagePath;
        return this;
    }

    public Gear build() {
        return new Gear(id, rarity, name, imagePath);
    }
}
package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.Message;
import com.imstargg.core.domain.MessageCollection;
import com.imstargg.core.enums.GearRarity;
import com.imstargg.core.enums.Language;
import com.imstargg.test.java.IntegerIncrementUtil;
import com.imstargg.test.java.LongIncrementUtil;

import javax.annotation.Nullable;
import java.util.Arrays;

public class GearFixture {
    private BrawlStarsId id = new BrawlStarsId(LongIncrementUtil.next());
    private GearRarity rarity = GearRarity.values()[IntegerIncrementUtil.next(GearRarity.values().length)];
    private MessageCollection names = new MessageCollection(
            "name-" + LongIncrementUtil.next(),
            Arrays.stream(Language.values())
                    .map(language -> new Message(language, "name-" + LongIncrementUtil.next()))
                    .toList()
    );

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

    public GearFixture names(MessageCollection names) {
        this.names = names;
        return this;
    }

    public GearFixture imagePath(@Nullable String imagePath) {
        this.imagePath = imagePath;
        return this;
    }

    public Gear build() {
        return new Gear(id, rarity, names, imagePath);
    }
}
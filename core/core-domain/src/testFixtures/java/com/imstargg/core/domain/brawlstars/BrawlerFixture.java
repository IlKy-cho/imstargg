package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.Message;
import com.imstargg.core.domain.MessageCollection;
import com.imstargg.core.enums.BrawlerRarity;
import com.imstargg.core.enums.BrawlerRole;
import com.imstargg.core.enums.Language;
import com.imstargg.test.java.IntegerIncrementUtil;
import com.imstargg.test.java.LongIncrementUtil;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class BrawlerFixture {
    private BrawlStarsId id = new BrawlStarsId(LongIncrementUtil.next());
    private MessageCollection names = new MessageCollection(
            "name-" + LongIncrementUtil.next(),
            Arrays.stream(Language.values())
                    .map(language -> new Message(language, "name-" + LongIncrementUtil.next()))
                    .toList()
    );
    private BrawlerRarity rarity = BrawlerRarity.values()[IntegerIncrementUtil.next(BrawlerRarity.values().length)];
    private BrawlerRole role = BrawlerRole.values()[IntegerIncrementUtil.next(BrawlerRole.values().length)];
    private List<Gadget> gadgets = List.of(new GadgetFixture().build());
    private List<Gear> gears = List.of(new GearFixture().build());
    private List<StarPower> starPowers = List.of(new StarPowerFixture().build());
    @Nullable
    private String imagePath = "/images/brawlers/" + LongIncrementUtil.next();

    public BrawlerFixture id(BrawlStarsId id) {
        this.id = id;
        return this;
    }

    public BrawlerFixture names(MessageCollection names) {
        this.names = names;
        return this;
    }

    public BrawlerFixture rarity(BrawlerRarity rarity) {
        this.rarity = rarity;
        return this;
    }

    public BrawlerFixture role(BrawlerRole role) {
        this.role = role;
        return this;
    }

    public BrawlerFixture gadgets(List<Gadget> gadgets) {
        this.gadgets = gadgets;
        return this;
    }

    public BrawlerFixture gears(List<Gear> gears) {
        this.gears = gears;
        return this;
    }

    public BrawlerFixture starPowers(List<StarPower> starPowers) {
        this.starPowers = starPowers;
        return this;
    }

    public BrawlerFixture imagePath(@Nullable String imagePath) {
        this.imagePath = imagePath;
        return this;
    }

    public Brawler build() {
        return new Brawler(id, names, rarity, role, gadgets, gears, starPowers, imagePath);
    }
}
package com.imstargg.client.brawlstars.response;

import com.imstargg.test.java.IntegerIncrementUtil;
import com.imstargg.test.java.LongIncrementUtil;

import java.util.List;
import java.util.stream.IntStream;

public class BrawlerStatResponseFixture {

    private long id = LongIncrementUtil.next();
    private String name = "brawlerName" + LongIncrementUtil.next();
    private int power = IntegerIncrementUtil.next(11 + 1);
    private int rank = IntegerIncrementUtil.next(51 + 1);
    private int trophies = IntegerIncrementUtil.next(10000 + 1);
    private int highestTrophies = IntegerIncrementUtil.next(10000 + 1);
    private List<GearStatResponse> gears = IntStream.range(0, IntegerIncrementUtil.next(2))
            .mapToObj(i -> new GearStatResponseFixture().build())
            .toList();
    private List<AccessoryResponse> gadgets = IntStream.range(0, IntegerIncrementUtil.next(2))
            .mapToObj(i -> new AccessoryResponseFixture().build())
            .toList();
    private List<StarPowerResponse> starPowers = IntStream.range(0, IntegerIncrementUtil.next(2))
            .mapToObj(i -> new StarPowerResponseFixture().build())
            .toList();

    public BrawlerStatResponseFixture id(long id) {
        this.id = id;
        return this;
    }

    public BrawlerStatResponseFixture name(String name) {
        this.name = name;
        return this;
    }

    public BrawlerStatResponseFixture power(int power) {
        this.power = power;
        return this;
    }

    public BrawlerStatResponseFixture rank(int rank) {
        this.rank = rank;
        return this;
    }

    public BrawlerStatResponseFixture trophies(int trophies) {
        this.trophies = trophies;
        return this;
    }

    public BrawlerStatResponseFixture highestTrophies(int highestTrophies) {
        this.highestTrophies = highestTrophies;
        return this;
    }

    public BrawlerStatResponseFixture gears(List<GearStatResponse> gears) {
        this.gears = gears;
        return this;
    }

    public BrawlerStatResponseFixture gadgets(List<AccessoryResponse> gadgets) {
        this.gadgets = gadgets;
        return this;
    }

    public BrawlerStatResponseFixture starPowers(List<StarPowerResponse> starPowers) {
        this.starPowers = starPowers;
        return this;
    }

    public BrawlerStatResponse build() {
        return new BrawlerStatResponse(
                id,
                name,
                power,
                rank,
                trophies,
                highestTrophies,
                gears,
                gadgets,
                starPowers
        );
    }
}

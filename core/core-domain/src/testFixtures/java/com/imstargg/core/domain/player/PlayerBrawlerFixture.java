package com.imstargg.core.domain.player;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.test.java.IntegerIncrementUtil;
import com.imstargg.test.java.LongIncrementUtil;

import java.util.List;

public class PlayerBrawlerFixture {

    private BrawlStarsId brawlStarsId = new BrawlStarsId(LongIncrementUtil.next());
    private List<BrawlStarsId> gearIds = List.of(new BrawlStarsId(LongIncrementUtil.next()));
    private List<BrawlStarsId> starPowerIds = List.of(new BrawlStarsId(LongIncrementUtil.next()));
    private List<BrawlStarsId> gadgetIds = List.of(new BrawlStarsId(LongIncrementUtil.next()));
    private int power = IntegerIncrementUtil.next(1, 11);
    private int rank = IntegerIncrementUtil.next(1, 50);
    private int trophies = IntegerIncrementUtil.next(0, 10000);
    private int highestTrophies = trophies;

    public PlayerBrawlerFixture brawlStarsId(BrawlStarsId brawlStarsId) {
        this.brawlStarsId = brawlStarsId;
        return this;
    }

    public PlayerBrawlerFixture gearIds(List<BrawlStarsId> gearIds) {
        this.gearIds = gearIds;
        return this;
    }

    public PlayerBrawlerFixture starPowerIds(List<BrawlStarsId> starPowerIds) {
        this.starPowerIds = starPowerIds;
        return this;
    }

    public PlayerBrawlerFixture gadgetIds(List<BrawlStarsId> gadgetIds) {
        this.gadgetIds = gadgetIds;
        return this;
    }

    public PlayerBrawlerFixture power(int power) {
        this.power = power;
        return this;
    }

    public PlayerBrawlerFixture rank(int rank) {
        this.rank = rank;
        return this;
    }

    public PlayerBrawlerFixture trophies(int trophies) {
        this.trophies = trophies;
        return this;
    }

    public PlayerBrawlerFixture highestTrophies(int highestTrophies) {
        this.highestTrophies = highestTrophies;
        return this;
    }

    public PlayerBrawler build() {
        return new PlayerBrawler(brawlStarsId, gearIds, starPowerIds, gadgetIds, power, rank, trophies, highestTrophies);
    }
} 
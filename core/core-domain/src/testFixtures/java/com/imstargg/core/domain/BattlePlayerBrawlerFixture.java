package com.imstargg.core.domain;


import com.imstargg.test.java.IntegerIncrementUtil;
import com.imstargg.test.java.LongIncrementUtil;

import javax.annotation.Nullable;
import java.util.concurrent.ThreadLocalRandom;

public class BattlePlayerBrawlerFixture {

    private BrawlStarsId brawlerId = new BrawlStarsId(LongIncrementUtil.next());
    private int power = ThreadLocalRandom.current().nextInt(1, 12);

    @Nullable
    private Integer trophies = ThreadLocalRandom.current().nextInt(0, 10000);

    @Nullable
    private Integer trophyChange = ThreadLocalRandom.current().nextInt(-13, 13);

    public BattlePlayerBrawlerFixture brawlerId(BrawlStarsId brawlerId) {
        this.brawlerId = brawlerId;
        return this;
    }

    public BattlePlayerBrawlerFixture power(int power) {
        this.power = power;
        return this;
    }

    public BattlePlayerBrawlerFixture trophies(@Nullable Integer trophies) {
        this.trophies = trophies;
        return this;
    }

    public BattlePlayerBrawlerFixture trophyChange(@Nullable Integer trophyChange) {
        this.trophyChange = trophyChange;
        return this;
    }

    public BattlePlayerBrawlerFixture soloRank() {
        this.trophies = IntegerIncrementUtil.next(19);
        this.trophyChange = null;
        return this;
    }

    public BattlePlayerBrawler build() {
        return new BattlePlayerBrawler(brawlerId, power, trophies, trophyChange);
    }
}
package com.imstargg.storage.db.core;

import com.imstargg.test.java.IntegerIncrementUtil;
import com.imstargg.test.java.LongIncrementUtil;

public class BattleCollectionEntityTeamPlayerBrawlerFixture {

    private long brawlStarsId = LongIncrementUtil.next();
    private String name = "brawler-" + LongIncrementUtil.next();
    private Integer power = IntegerIncrementUtil.next(11 + 1);
    private Integer trophies = IntegerIncrementUtil.next(10000);
    private Integer trophyChange = IntegerIncrementUtil.next(13 + 1);

    public BattleCollectionEntityTeamPlayerBrawlerFixture brawlStarsId(Long brawlStarsId) {
        this.brawlStarsId = brawlStarsId;
        return this;
    }

    public BattleCollectionEntityTeamPlayerBrawlerFixture name(String name) {
        this.name = name;
        return this;
    }

    public BattleCollectionEntityTeamPlayerBrawlerFixture power(Integer power) {
        this.power = power;
        return this;
    }

    public BattleCollectionEntityTeamPlayerBrawlerFixture trophies(Integer trophies) {
        this.trophies = trophies;
        return this;
    }

    public BattleCollectionEntityTeamPlayerBrawlerFixture trophyChange(Integer trophyChange) {
        this.trophyChange = trophyChange;
        return this;
    }

    public BattleCollectionEntityTeamPlayerBrawler build() {
        return new BattleCollectionEntityTeamPlayerBrawler(
            brawlStarsId,
            name,
            power,
            trophies,
            trophyChange
        );
    }

}

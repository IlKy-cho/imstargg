package com.imstargg.storage.db.core;

import com.imstargg.test.java.LongIncrementUtil;

public class BattleCollectionEntityTeamPlayerFixture {

    private String brawlStarsTag = "#TAG" + LongIncrementUtil.next();
    private String name = "name-" + LongIncrementUtil.next();
    private BattleCollectionEntityTeamPlayerBrawler brawler = new BattleCollectionEntityTeamPlayerBrawlerFixture().build();

    public BattleCollectionEntityTeamPlayerFixture brawlStarsTag(String brawlStarsTag) {
        this.brawlStarsTag = brawlStarsTag;
        return this;
    }

    public BattleCollectionEntityTeamPlayerFixture name(String name) {
        this.name = name;
        return this;
    }

    public BattleCollectionEntityTeamPlayerFixture brawler(BattleCollectionEntityTeamPlayerBrawler brawler) {
        this.brawler = brawler;
        return this;
    }

    public BattleCollectionEntityTeamPlayer build() {
        return new BattleCollectionEntityTeamPlayer(brawlStarsTag, name, brawler);
    }
}

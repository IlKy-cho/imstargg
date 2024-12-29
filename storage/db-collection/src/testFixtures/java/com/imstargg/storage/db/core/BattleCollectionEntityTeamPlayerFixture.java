package com.imstargg.storage.db.core;

import com.imstargg.test.java.LongIncrementUtil;
import jakarta.annotation.Nullable;

public class BattleCollectionEntityTeamPlayerFixture {

    @Nullable
    private String brawlStarsTag;

    @Nullable
    private String name;

    @Nullable
    private BattleCollectionEntityTeamPlayerBrawler brawler;

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
        fillRequiredFields();
        return new BattleCollectionEntityTeamPlayer(brawlStarsTag, name, brawler);
    }

    private void fillRequiredFields() {
        long fillKey = LongIncrementUtil.next();
        if (brawlStarsTag == null) {
            brawlStarsTag = "brawlStarsTag-" + fillKey;
        }
        if (name == null) {
            name = "name-" + fillKey;
        }
        if (brawler == null) {
            brawler = new BattleCollectionEntityTeamPlayerBrawlerFixture().build();
        }
    }
}

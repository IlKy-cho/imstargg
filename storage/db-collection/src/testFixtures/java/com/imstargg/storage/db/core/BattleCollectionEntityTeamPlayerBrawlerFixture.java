package com.imstargg.storage.db.core;

import com.imstargg.test.java.LongIncrementUtil;
import jakarta.annotation.Nullable;

public class BattleCollectionEntityTeamPlayerBrawlerFixture {

    @Nullable
    private Long brawlStarsId;

    @Nullable
    private String name;

    @Nullable
    private Integer power;

    @Nullable
    private Integer trophies;

    @Nullable
    private Integer trophyChange;

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
        fillRequiredFields();
        return new BattleCollectionEntityTeamPlayerBrawler(
            brawlStarsId,
            name,
            power,
            trophies,
            trophyChange
        );
    }

    private void fillRequiredFields() {
        long fillKey = LongIncrementUtil.next();
        if (brawlStarsId == null) {
            brawlStarsId = fillKey;
        }
        if (name == null) {
            name = "name-" + fillKey;
        }
        if (power == null) {
            power = (int) fillKey;
        }
        if (trophies == null) {
            trophies = (int) fillKey;
        }
        if (trophyChange == null) {
            trophyChange = (int) fillKey;
        }
    }
}

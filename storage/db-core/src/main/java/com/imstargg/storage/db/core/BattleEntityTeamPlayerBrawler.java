package com.imstargg.storage.db.core;

import jakarta.annotation.Nullable;

public class BattleEntityTeamPlayerBrawler {

    private long brawlStarsId;

    private String name;

    private int power;

    @Nullable
    private Integer trophies;

    @Nullable
    private Integer trophyChange;

    protected BattleEntityTeamPlayerBrawler() {
    }

    public long getBrawlStarsId() {
        return brawlStarsId;
    }

    public String getName() {
        return name;
    }

    public int getPower() {
        return power;
    }

    @Nullable
    public Integer getTrophies() {
        return trophies;
    }

    @Nullable
    public Integer getTrophyChange() {
        return trophyChange;
    }
}

package com.imstargg.storage.db.core;

import jakarta.annotation.Nullable;

public class BattleCollectionEntityTeamPlayerBrawler {

    private long brawlStarsId;

    private String name;

    private int power;

    @Nullable
    private Integer trophies;

    @Nullable
    private Integer trophyChange;

    protected BattleCollectionEntityTeamPlayerBrawler() {
    }

    public BattleCollectionEntityTeamPlayerBrawler(
            long brawlStarsId,
            String name,
            int power,
            @Nullable Integer trophies,
            @Nullable Integer trophyChange
    ) {
        this.brawlStarsId = brawlStarsId;
        this.name = name;
        this.power = power;
        this.trophies = trophies;
        this.trophyChange = trophyChange;
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

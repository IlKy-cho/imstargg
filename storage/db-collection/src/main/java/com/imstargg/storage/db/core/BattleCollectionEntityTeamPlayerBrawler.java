package com.imstargg.storage.db.core;

import jakarta.annotation.Nullable;

public class BattleCollectionEntityTeamPlayerBrawler {

    private long brawlStarsId;

    private String name;

    private int power;

    private int trophies;

    @Nullable
    private Integer trophyChange;

    protected BattleCollectionEntityTeamPlayerBrawler() {
    }

    public BattleCollectionEntityTeamPlayerBrawler(
            long brawlStarsId,
            String name,
            int power,
            int trophies,
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

    public int getTrophies() {
        return trophies;
    }

    @Nullable
    public Integer getTrophyChange() {
        return trophyChange;
    }
}

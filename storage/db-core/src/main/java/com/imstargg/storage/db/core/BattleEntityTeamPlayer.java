package com.imstargg.storage.db.core;

public class BattleEntityTeamPlayer {

    private String brawlStarsTag;

    private String name;

    private BattleEntityTeamPlayerBrawler brawler;

    protected BattleEntityTeamPlayer() {
    }

    public String getBrawlStarsTag() {
        return brawlStarsTag;
    }

    public String getName() {
        return name;
    }

    public BattleEntityTeamPlayerBrawler getBrawler() {
        return brawler;
    }
}

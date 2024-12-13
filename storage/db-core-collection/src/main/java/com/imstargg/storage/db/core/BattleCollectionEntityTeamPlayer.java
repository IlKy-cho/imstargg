package com.imstargg.storage.db.core;

public class BattleCollectionEntityTeamPlayer {

    private String brawlStarsTag;

    private String name;

    private BattleCollectionEntityTeamPlayerBrawler brawler;

    protected BattleCollectionEntityTeamPlayer() {
    }

    public BattleCollectionEntityTeamPlayer(
            String brawlStarsTag, String name, BattleCollectionEntityTeamPlayerBrawler brawler) {
        this.brawlStarsTag = brawlStarsTag;
        this.name = name;
        this.brawler = brawler;
    }

    public String getBrawlStarsTag() {
        return brawlStarsTag;
    }

    public String getName() {
        return name;
    }

    public BattleCollectionEntityTeamPlayerBrawler getBrawler() {
        return brawler;
    }
}

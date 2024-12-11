package com.imstargg.storage.db.core;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class BattlePlayerEntityBrawler {

    @Column(name = "brawler_brawlstars_id", updatable = false, nullable = false)
    private long brawlStarsId;

    @Column(name = "brawler_name", length = 65, updatable = false, nullable = false)
    private String name;

    @Column(name = "brawler_power", updatable = false, nullable = false)
    private int power;

    @Nullable
    @Column(name = "brawler_trophies", updatable = false)
    private Integer trophies;

    @Nullable
    @Column(name = "brawler_trophy_change", updatable = false)
    private Integer trophyChange;

    protected BattlePlayerEntityBrawler() {
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

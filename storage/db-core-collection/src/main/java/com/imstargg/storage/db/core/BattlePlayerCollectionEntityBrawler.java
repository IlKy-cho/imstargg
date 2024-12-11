package com.imstargg.storage.db.core;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class BattlePlayerCollectionEntityBrawler {

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

    protected BattlePlayerCollectionEntityBrawler() {
    }

    public BattlePlayerCollectionEntityBrawler(
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

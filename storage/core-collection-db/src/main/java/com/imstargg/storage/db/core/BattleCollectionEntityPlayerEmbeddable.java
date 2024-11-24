package com.imstargg.storage.db.core;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class BattleCollectionEntityPlayerEmbeddable {

    @Column(name = "player_id", updatable = false, nullable = false)
    private long playerId;

    @Column(name = "brawler_id", updatable = false, nullable = false)
    private long brawlerId;

    @Column(name = "power", updatable = false, nullable = false)
    private int power;

    @Nullable
    @Column(name = "brawler_trophies", updatable = false)
    private Integer brawlerTrophies;

    @Nullable
    @Column(name = "trophy_change", updatable = false)
    private Integer trophyChange;

    @Nullable
    @Column(name = "trophy_snapshot", updatable = false)
    private Integer trophySnapshot;

    @Nullable
    @Column(name = "brawler_trophy_snapshot", updatable = false)
    private Integer brawlerTrophySnapshot;

    protected BattleCollectionEntityPlayerEmbeddable() {
    }

    public BattleCollectionEntityPlayerEmbeddable(
            long playerId,
            long brawlerId,
            int power,
            @Nullable Integer brawlerTrophies,
            @Nullable Integer trophyChange,
            @Nullable Integer trophySnapshot,
            @Nullable Integer brawlerTrophySnapshot
    ) {
        this.playerId = playerId;
        this.brawlerId = brawlerId;
        this.power = power;
        this.brawlerTrophies = brawlerTrophies;
        this.trophyChange = trophyChange;
        this.trophySnapshot = trophySnapshot;
        this.brawlerTrophySnapshot = brawlerTrophySnapshot;
    }

    public long getPlayerId() {
        return playerId;
    }

    public long getBrawlerId() {
        return brawlerId;
    }

    public int getPower() {
        return power;
    }

    @Nullable
    public Integer getBrawlerTrophies() {
        return brawlerTrophies;
    }

    @Nullable
    public Integer getTrophyChange() {
        return trophyChange;
    }

    @Nullable
    public Integer getTrophySnapshot() {
        return trophySnapshot;
    }

    @Nullable
    public Integer getBrawlerTrophySnapshot() {
        return brawlerTrophySnapshot;
    }
}

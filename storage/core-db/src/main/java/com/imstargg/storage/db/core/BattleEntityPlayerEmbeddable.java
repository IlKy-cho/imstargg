package com.imstargg.storage.db.core;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class BattleEntityPlayerEmbeddable {

    @Column(name = "player_id", updatable = false, nullable = false)
    private long playerId;

    @Column(name = "brawler_brawlstars_id", updatable = false, nullable = false)
    private long brawlerBrawlStarsId;

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

    protected BattleEntityPlayerEmbeddable() {
    }

}

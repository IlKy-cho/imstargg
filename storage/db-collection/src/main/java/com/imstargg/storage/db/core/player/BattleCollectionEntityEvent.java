package com.imstargg.storage.db.core.player;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class BattleCollectionEntityEvent {

    @Nullable
    @Column(name = "event_brawlstars_id", updatable = false)
    private Long brawlStarsId;

    @Nullable
    @Column(name = "event_mode", length = 65, updatable = false)
    private String mode;

    @Nullable
    @Column(name = "event_map", length = 65, updatable = false)
    private String map;

    protected BattleCollectionEntityEvent() {
    }

    public BattleCollectionEntityEvent(long brawlStarsId, String mode, String map) {
        this.brawlStarsId = brawlStarsId;
        this.mode = mode;
        this.map = map;
    }

    @Nullable
    public Long getBrawlStarsId() {
        return brawlStarsId;
    }

    public String getMode() {
        return mode;
    }

    public String getMap() {
        return map;
    }
}

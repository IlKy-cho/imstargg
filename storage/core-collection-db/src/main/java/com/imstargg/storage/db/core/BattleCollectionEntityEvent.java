package com.imstargg.storage.db.core;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class BattleCollectionEntityEvent {

    @Column(name = "event_brawlstars_id", updatable = false, nullable = false)
    private long eventBrawlStarsId;

    @Column(name = "event_mode", length = 65, updatable = false, nullable = false)
    private String mode;

    @Column(name = "event_map", length = 65, updatable = false, nullable = false)
    private String map;

    protected BattleCollectionEntityEvent() {
    }

    public BattleCollectionEntityEvent(long eventBrawlStarsId, String mode, String map) {
        this.eventBrawlStarsId = eventBrawlStarsId;
        this.mode = mode;
        this.map = map;
    }

    public long getEventBrawlStarsId() {
        return eventBrawlStarsId;
    }

    public String getMode() {
        return mode;
    }

    public String getMap() {
        return map;
    }
}

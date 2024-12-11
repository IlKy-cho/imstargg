package com.imstargg.storage.db.core;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class BattleEntityEvent {

    @Nullable
    @Column(name = "event_brawlstars_id", updatable = false)
    private Long eventBrawlStarsId;

    @Nullable
    @Column(name = "event_mode", length = 65, updatable = false)
    private String mode;

    @Nullable
    @Column(name = "event_map", length = 65, updatable = false)
    private String map;

    protected BattleEntityEvent() {
    }

    @Nullable
    public Long getEventBrawlStarsId() {
        return eventBrawlStarsId;
    }

    public String getMode() {
        return mode;
    }

    public String getMap() {
        return map;
    }
}

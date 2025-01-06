package com.imstargg.storage.db.core;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class BattleEntityEvent {

    @Nullable
    @Column(name = "event_brawlstars_id", updatable = false)
    private Long brawlStarsId;

    @Nullable
    @Column(name = "event_mode", length = 65, updatable = false)
    private String mode;

    @Nullable
    @Column(name = "event_map", length = 65, updatable = false)
    private String map;

    protected BattleEntityEvent() {
    }

    @Nullable
    public Long getBrawlStarsId() {
        return brawlStarsId;
    }

    @Nullable
    public String getMode() {
        return mode;
    }

    @Nullable
    public String getMap() {
        return map;
    }
}

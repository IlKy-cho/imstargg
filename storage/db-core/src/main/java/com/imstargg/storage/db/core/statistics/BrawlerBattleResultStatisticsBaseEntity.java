package com.imstargg.storage.db.core.statistics;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
abstract class BrawlerBattleResultStatisticsBaseEntity extends BattleStatisticsBaseEntity {

    @Column(name = "victory_count", nullable = false, updatable = false)
    private long victoryCount;

    @Column(name = "defeat_count", nullable = false, updatable = false)
    private long defeatCount;

    @Column(name = "draw_count", nullable = false, updatable = false)
    private long drawCount;

    protected BrawlerBattleResultStatisticsBaseEntity() {
    }

    public long getVictoryCount() {
        return victoryCount;
    }

    public long getDefeatCount() {
        return defeatCount;
    }

    public long getDrawCount() {
        return drawCount;
    }
}

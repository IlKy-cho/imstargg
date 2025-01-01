package com.imstargg.storage.db.core.statistics;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class BattleStatisticsEntityBrawlers {

    @Column(name = "brawler_num", updatable = false, nullable = false)
    private int num;

    @Column(name = "brawler_brawlstars_id_hash", length = 60, updatable = false, nullable = false)
    private byte[] idHash;

    protected BattleStatisticsEntityBrawlers() {
    }

    public int getNum() {
        return num;
    }

    public byte[] getIdHash() {
        return idHash;
    }
}

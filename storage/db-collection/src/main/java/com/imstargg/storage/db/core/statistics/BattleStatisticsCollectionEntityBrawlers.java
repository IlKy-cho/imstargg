package com.imstargg.storage.db.core.statistics;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class BattleStatisticsCollectionEntityBrawlers {

    @Column(name = "brawler_num", updatable = false, nullable = false)
    private int num;

    @Column(name = "brawler_brawlstars_id_hash", length = 60, updatable = false, nullable = false)
    private String idHash;

    protected BattleStatisticsCollectionEntityBrawlers() {
    }

    public BattleStatisticsCollectionEntityBrawlers(int num, String idHash) {
        this.num = num;
        this.idHash = idHash;
    }

    public int getNum() {
        return num;
    }

    public String getIdHash() {
        return idHash;
    }
}

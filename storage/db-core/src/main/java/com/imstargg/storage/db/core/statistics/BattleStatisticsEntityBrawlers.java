package com.imstargg.storage.db.core.statistics;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.List;

@Embeddable
public class BattleStatisticsEntityBrawlers {

    @Column(name = "brawler_num", updatable = false, nullable = false)
    private int num;

    @Column(name = "brawler_brawlstars_id_hash", length = 60, updatable = false, nullable = false)
    private byte[] idHash;

    protected BattleStatisticsEntityBrawlers() {
    }

    public BattleStatisticsEntityBrawlers(IdHash idHash) {
        this.num = idHash.num();
        this.idHash = idHash.value();
    }

    public List<Long> getBrawlerBrawlStarsIds() {
        return new IdHash(idHash).ids();
    }

    public int getNum() {
        return num;
    }

    public byte[] getIdHash() {
        return idHash;
    }
}

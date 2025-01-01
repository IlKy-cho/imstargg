package com.imstargg.storage.db.core.statistics;

import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
abstract class BrawlerBattleResultBaseEntity extends BattleStatisticsBaseEntity {

    @Nullable
    @Enumerated(EnumType.STRING)
    @Column(name = "solo_rank_tier_range", length = 25, updatable = false)
    private SoloRankTierRange soloRankTierRange;

    @Nullable
    @Enumerated(EnumType.STRING)
    @Column(name = "trophy_range", length = 25, updatable = false)
    private TrophyRange trophyRange;

    @Column(name = "duplicate_brawler", nullable = false, updatable = false)
    private boolean duplicateBrawler;

    @Column(name = "victory_count", nullable = false)
    private int victoryCount;

    @Column(name = "defeat_count", nullable = false)
    private int defeatCount;

    @Column(name = "draw_count", nullable = false)
    private int drawCount;

    protected BrawlerBattleResultBaseEntity() {
    }

    @Nullable
    public SoloRankTierRange getSoloRankTierRange() {
        return soloRankTierRange;
    }

    @Nullable
    public TrophyRange getTrophyRange() {
        return trophyRange;
    }

    public boolean isDuplicateBrawler() {
        return duplicateBrawler;
    }

    public int getVictoryCount() {
        return victoryCount;
    }

    public int getDefeatCount() {
        return defeatCount;
    }

    public int getDrawCount() {
        return drawCount;
    }
}

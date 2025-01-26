package com.imstargg.storage.db.core.statistics;

import com.imstargg.core.enums.BattleResult;
import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDate;

@MappedSuperclass
abstract class BrawlerBattleResultStatisticsBaseCollectionEntity extends BattleStatisticsBaseCollectionEntity {

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
    private long victoryCount;

    @Column(name = "defeat_count", nullable = false)
    private long defeatCount;

    @Column(name = "draw_count", nullable = false)
    private long drawCount;

    protected BrawlerBattleResultStatisticsBaseCollectionEntity() {
    }

    protected BrawlerBattleResultStatisticsBaseCollectionEntity(
            long battleEventId,
            LocalDate battleDate,
            @Nullable TrophyRange trophyRange,
            @Nullable SoloRankTierRange soloRankTierRange,
            boolean duplicateBrawler
    ) {
        super(battleEventId, battleDate);
        if (soloRankTierRange == null && trophyRange == null) {
            throw new IllegalArgumentException("Either soloRankTierRange or trophyRange must be set.");
        }
        this.soloRankTierRange = soloRankTierRange;
        this.trophyRange = trophyRange;
        this.duplicateBrawler = duplicateBrawler;
        this.victoryCount = 0;
        this.defeatCount = 0;
        this.drawCount = 0;
    }

    public void countUp(BattleResult result) {
        switch (result) {
            case VICTORY:
                victoryCount++;
                break;
            case DEFEAT:
                defeatCount++;
                break;
            case DRAW:
                drawCount++;
                break;
        }
    }

    public void init() {
        victoryCount = 0;
        defeatCount = 0;
        drawCount = 0;
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

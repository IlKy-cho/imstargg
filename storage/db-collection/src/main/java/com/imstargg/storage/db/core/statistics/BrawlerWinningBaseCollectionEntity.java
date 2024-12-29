package com.imstargg.storage.db.core.statistics;

import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDate;

@MappedSuperclass
abstract class BrawlerWinningBaseCollectionEntity extends BrawlerStatisticsBaseCollectionEntity {

    @Nullable
    @Enumerated(EnumType.STRING)
    @Column(name = "solo_rank_tier_range", length = 25, updatable = false)
    private SoloRankTierRange soloRankTierRange;

    @Nullable
    @Enumerated(EnumType.STRING)
    @Column(name = "trophy_range", length = 25, updatable = false)
    private TrophyRange trophyRange;

    @Column(name = "total_count", nullable = false)
    private int totalCount;

    @Column(name = "win_count", nullable = false)
    private int winCount;

    protected BrawlerWinningBaseCollectionEntity() {
    }

    protected BrawlerWinningBaseCollectionEntity(
            @Nullable SoloRankTierRange soloRankTierRange,
            @Nullable TrophyRange trophyRange,
            long battleEventId,
            LocalDate battleDate,
            long brawlerBrawlStarsId
    ) {
        super(battleEventId, battleDate, brawlerBrawlStarsId);
        this.totalCount = 0;
        this.winCount = 0;
    }

    public void win() {
        winCount++;
        totalCount++;
    }

    public void lose() {
        totalCount++;
    }

    @Nullable
    public SoloRankTierRange getSoloRankTierRange() {
        return soloRankTierRange;
    }

    @Nullable
    public TrophyRange getTrophyRange() {
        return trophyRange;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getWinCount() {
        return winCount;
    }
}

package com.imstargg.storage.db.core.statistics;

import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
abstract class BrawlerWinningBaseEntity extends BattleStatisticsBaseEntity {

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

    @Column(name = "total_count", updatable = false, nullable = false)
    private int totalCount;

    @Column(name = "win_count", updatable = false, nullable = false)
    private int winCount;

    public int getTotalCount() {
        return totalCount;
    }

    public int getWinCount() {
        return winCount;
    }
}

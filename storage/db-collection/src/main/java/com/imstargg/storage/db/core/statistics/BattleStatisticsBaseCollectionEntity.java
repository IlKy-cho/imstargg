package com.imstargg.storage.db.core.statistics;

import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDate;

@MappedSuperclass
abstract class BattleStatisticsBaseCollectionEntity extends BaseEntity {

    @Column(name = "event_brawlstars_id", nullable = false, updatable = false)
    private long eventBrawlStarsId;

    @Column(name = "brawler_brawlstars_id", nullable = false, updatable = false)
    private long brawlerBrawlStarsId;

    @Column(name = "tier_range", length = 25, updatable = false)
    private String tierRange;

    @Column(name = "battle_date", nullable = false, updatable = false)
    private LocalDate battleDate;

    protected BattleStatisticsBaseCollectionEntity() {
    }

    protected BattleStatisticsBaseCollectionEntity(
            long eventBrawlStarsId,
            long brawlerBrawlStarsId,
            SoloRankTierRange soloRankTierRange,
            LocalDate battleDate
    ) {
        this.eventBrawlStarsId = eventBrawlStarsId;
        this.tierRange = soloRankTierRange.name();
        this.brawlerBrawlStarsId = brawlerBrawlStarsId;
        this.battleDate = battleDate;
    }

    protected BattleStatisticsBaseCollectionEntity(
            long eventBrawlStarsId,
            long brawlerBrawlStarsId,
            TrophyRange trophyRange,
            LocalDate battleDate
    ) {
        this.eventBrawlStarsId = eventBrawlStarsId;
        this.tierRange = trophyRange.name();
        this.brawlerBrawlStarsId = brawlerBrawlStarsId;
        this.battleDate = battleDate;
    }

    protected BattleStatisticsBaseCollectionEntity(
            long eventBrawlStarsId,
            long brawlerBrawlStarsId,
            LocalDate battleDate
    ) {
        this.eventBrawlStarsId = eventBrawlStarsId;
        this.brawlerBrawlStarsId = brawlerBrawlStarsId;
        this.battleDate = battleDate;
    }

    public long getEventBrawlStarsId() {
        return eventBrawlStarsId;
    }

    public String getTierRange() {
        return tierRange;
    }

    public long getBrawlerBrawlStarsId() {
        return brawlerBrawlStarsId;
    }

    public LocalDate getBattleDate() {
        return battleDate;
    }
}

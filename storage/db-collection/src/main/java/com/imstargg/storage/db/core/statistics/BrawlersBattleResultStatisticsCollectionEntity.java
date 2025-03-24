package com.imstargg.storage.db.core.statistics;

import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "brawlers_battle_result_stats_v3")
public class BrawlersBattleResultStatisticsCollectionEntity extends BrawlerBattleResultStatisticsBaseCollectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brawlers_battle_result_stats_id")
    private Long id;

    @Column(name = "pair_brawler_brawlstars_id", updatable = false, nullable = false)
    private long pairBrawlerBrawlStarsId;

    protected BrawlersBattleResultStatisticsCollectionEntity() {
    }

    public BrawlersBattleResultStatisticsCollectionEntity(
            long eventBrawlStarsId,
            long brawlerBrawlStarsId,
            TrophyRange trophyRange,
            LocalDate battleDate,
            long pairBrawlerBrawlStarsId
    ) {

        super(eventBrawlStarsId, brawlerBrawlStarsId, trophyRange, battleDate);
        this.pairBrawlerBrawlStarsId = pairBrawlerBrawlStarsId;
    }

    public BrawlersBattleResultStatisticsCollectionEntity(
            long eventBrawlStarsId,
            long brawlerBrawlStarsId,
            SoloRankTierRange soloRankTierRange,
            LocalDate battleDate,
            long pairBrawlerBrawlStarsId
    ) {
        super(eventBrawlStarsId, brawlerBrawlStarsId, soloRankTierRange, battleDate);
        this.pairBrawlerBrawlStarsId = pairBrawlerBrawlStarsId;
    }

    public BrawlersBattleResultStatisticsCollectionEntity(
            long eventBrawlStarsId,
            long brawlerBrawlStarsId,
            LocalDate battleDate,
            long pairBrawlerBrawlStarsId
    ) {
        super(eventBrawlStarsId, brawlerBrawlStarsId, battleDate);
        this.pairBrawlerBrawlStarsId = pairBrawlerBrawlStarsId;
    }

    public Long getId() {
        return id;
    }

    public long getPairBrawlerBrawlStarsId() {
        return pairBrawlerBrawlStarsId;
    }
}

package com.imstargg.storage.db.core.statistics;

import com.imstargg.core.enums.TrophyRange;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "brawlers_rank_stats")
public class BrawlersRankStatisticsCollectionEntity extends BrawlerBattleRankStatisticsBaseCollectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brawler_rank_stats_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "trophy_range", length = 25, updatable = false, nullable = false)
    private TrophyRange trophyRange;

    @Column(name = "brawler_brawlstars_id", updatable = false, nullable = false)
    private long brawlerBrawlStarsId;

    @Embedded
    private BattleStatisticsCollectionEntityBrawlers brawlers;


    protected BrawlersRankStatisticsCollectionEntity() {
    }

    public BrawlersRankStatisticsCollectionEntity(
            long battleEventId,
            LocalDate battleDate,
            int rank,
            TrophyRange trophyRange,
            long brawlerBrawlStarsId,
            BattleStatisticsCollectionEntityBrawlers brawlers
    ) {
        super(battleEventId, battleDate, rank);
        this.trophyRange = trophyRange;
        this.brawlerBrawlStarsId = brawlerBrawlStarsId;
        this.brawlers = brawlers;
    }

    public Long getId() {
        return id;
    }

    public TrophyRange getTrophyRange() {
        return trophyRange;
    }

    public long getBrawlerBrawlStarsId() {
        return brawlerBrawlStarsId;
    }

    public BattleStatisticsCollectionEntityBrawlers getBrawlers() {
        return brawlers;
    }
}

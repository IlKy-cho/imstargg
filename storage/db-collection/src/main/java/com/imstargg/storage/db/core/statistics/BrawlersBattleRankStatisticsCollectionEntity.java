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
@Table(name = "brawlers_battle_rank_stats")
public class BrawlersBattleRankStatisticsCollectionEntity extends BrawlerBattleRankStatisticsBaseCollectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brawlers_battle_rank_stats_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "trophy_range", length = 25, updatable = false, nullable = false)
    private TrophyRange trophyRange;

    @Column(name = "brawler_brawlstars_id", updatable = false, nullable = false)
    private long brawlerBrawlStarsId;

    @Embedded
    private BattleStatisticsEntityBrawlers brawlers;


    protected BrawlersBattleRankStatisticsCollectionEntity() {
    }

    public BrawlersBattleRankStatisticsCollectionEntity(
            long battleEventId,
            LocalDate battleDate,
            TrophyRange trophyRange,
            long brawlerBrawlStarsId,
            BattleStatisticsEntityBrawlers brawlers
    ) {
        super(battleEventId, battleDate);
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

    public BattleStatisticsEntityBrawlers getBrawlers() {
        return brawlers;
    }
}

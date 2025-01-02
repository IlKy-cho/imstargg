package com.imstargg.storage.db.core.statistics;

import com.imstargg.core.enums.TrophyRange;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "brawler_rank_stats")
public class BrawlerRankStatisticsCollectionEntity extends BattleStatisticsBaseCollectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brawler_rank_stats_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "trophy_range", length = 25, updatable = false, nullable = false)
    private TrophyRange trophyRange;

    @Column(name = "brawler_brawlstars_id", updatable = false, nullable = false)
    private long brawlerBrawlStarsId;

    @Column(name = "rank_value", updatable = false, nullable = false)
    private int rank;

    @Column(name = "rank_count", nullable = false)
    private int count;

    protected BrawlerRankStatisticsCollectionEntity() {
    }

    public BrawlerRankStatisticsCollectionEntity(
            long battleEventId,
            LocalDate battleDate,
            long brawlerBrawlStarsId,
            TrophyRange trophyRange,
            int rank
    ) {
        super(battleEventId, battleDate);
        this.trophyRange = trophyRange;
        this.brawlerBrawlStarsId = brawlerBrawlStarsId;
        this.rank = rank;
        this.count = 0;
    }

    public void countUp() {
        count++;
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

    public int getRank() {
        return rank;
    }

    public int getCount() {
        return count;
    }
}

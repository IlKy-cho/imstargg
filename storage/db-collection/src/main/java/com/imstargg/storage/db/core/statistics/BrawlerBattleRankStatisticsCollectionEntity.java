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

@Entity
@Table(name = "brawler_battle_rank_stats_v2")
public class BrawlerBattleRankStatisticsCollectionEntity extends BrawlerBattleRankStatisticsBaseCollectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brawler_battle_rank_stats_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "trophy_range", length = 25, updatable = false, nullable = false)
    private TrophyRange trophyRange;

    @Column(name = "brawler_brawlstars_id", updatable = false, nullable = false)
    private long brawlerBrawlStarsId;


    protected BrawlerBattleRankStatisticsCollectionEntity() {
    }

    public BrawlerBattleRankStatisticsCollectionEntity(
            long battleEventId,
            int seasonNumber,
            TrophyRange trophyRange,
            long brawlerBrawlStarsId
    ) {
        super(battleEventId, seasonNumber);
        this.trophyRange = trophyRange;
        this.brawlerBrawlStarsId = brawlerBrawlStarsId;
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

}

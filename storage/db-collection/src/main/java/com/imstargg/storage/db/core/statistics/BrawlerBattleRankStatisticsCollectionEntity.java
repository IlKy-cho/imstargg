package com.imstargg.storage.db.core.statistics;

import com.imstargg.core.enums.TrophyRange;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "brawler_battle_rank_stats_v3")
public class BrawlerBattleRankStatisticsCollectionEntity extends BrawlerBattleRankStatisticsBaseCollectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brawler_battle_rank_stats_id")
    private Long id;

    protected BrawlerBattleRankStatisticsCollectionEntity() {
    }

    public BrawlerBattleRankStatisticsCollectionEntity(
            long eventBrawlStarsId,
            long brawlerBrawlStarsId,
            TrophyRange trophyRange,
            LocalDate battleDate
    ) {
        super(eventBrawlStarsId, brawlerBrawlStarsId, trophyRange, battleDate);
    }

    public Long getId() {
        return id;
    }

}

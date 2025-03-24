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
@Table(name = "brawler_battle_result_stats_v3")
public class BrawlerBattleResultStatisticsCollectionEntity extends BrawlerBattleResultStatisticsBaseCollectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brawler_battle_result_stats_id")
    private Long id;

    @Column(name = "star_player_count", nullable = false)
    private long starPlayerCount;

    protected BrawlerBattleResultStatisticsCollectionEntity() {
    }

    public BrawlerBattleResultStatisticsCollectionEntity(
            long eventBrawlStarsId,
            long brawlerBrawlStarsId,
            TrophyRange trophyRange,
            LocalDate battleDate
    ) {
        super(eventBrawlStarsId, brawlerBrawlStarsId, trophyRange, battleDate);
        this.starPlayerCount = 0;
    }

    public BrawlerBattleResultStatisticsCollectionEntity(
            long eventBrawlStarsId,
            long brawlerBrawlStarsId,
            SoloRankTierRange soloRankTierRange,
            LocalDate battleDate
    ) {
        super(eventBrawlStarsId, brawlerBrawlStarsId, soloRankTierRange, battleDate);
        this.starPlayerCount = 0;
    }

    public BrawlerBattleResultStatisticsCollectionEntity(
            long eventBrawlStarsId,
            long brawlerBrawlStarsId,
            LocalDate battleDate
    ) {
        super(eventBrawlStarsId, brawlerBrawlStarsId, battleDate);
        this.starPlayerCount = 0;
    }

    public void starPlayer() {
        starPlayerCount++;
    }

    public Long getId() {
        return id;
    }

    public long getStarPlayerCount() {
        return starPlayerCount;
    }
}

package com.imstargg.storage.db.core.statistics;

import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "brawler_battle_result_stats_v2")
public class BrawlerBattleResultStatisticsCollectionEntity extends BrawlerBattleResultStatisticsBaseCollectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brawler_battle_result_stats_id")
    private Long id;

    @Column(name = "brawler_brawlstars_id", updatable = false, nullable = false)
    private long brawlerBrawlStarsId;

    @Column(name = "star_player_count", nullable = false)
    private long starPlayerCount;

    protected BrawlerBattleResultStatisticsCollectionEntity() {
    }

    public BrawlerBattleResultStatisticsCollectionEntity(
            long battleEventId,
            LocalDate battleDate,
            @Nullable TrophyRange trophyRange,
            @Nullable SoloRankTierRange soloRankTierRange,
            long brawlerBrawlStarsId
    ) {
        super(battleEventId, battleDate, trophyRange, soloRankTierRange);
        this.brawlerBrawlStarsId = brawlerBrawlStarsId;
        this.starPlayerCount = 0;
    }

    public void starPlayer() {
        starPlayerCount++;
    }

    @Override
    public void init() {
        super.init();
        starPlayerCount = 0;
    }

    public Long getId() {
        return id;
    }

    public long getBrawlerBrawlStarsId() {
        return brawlerBrawlStarsId;
    }

    public long getStarPlayerCount() {
        return starPlayerCount;
    }
}

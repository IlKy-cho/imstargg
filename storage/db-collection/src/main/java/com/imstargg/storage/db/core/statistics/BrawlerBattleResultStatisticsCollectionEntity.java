package com.imstargg.storage.db.core.statistics;

import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "brawler_battle_result_stats")
public class BrawlerBattleResultStatisticsCollectionEntity extends BrawlerBattleResultStatisticsBaseCollectionEntity {

    @Id
    @Column(name = "brawler_battle_result_stats_id")
    private Long id;

    @Column(name = "brawler_brawlstars_id", updatable = false, nullable = false)
    private long brawlerBrawlStarsId;

    @Column(name = "enemy_brawler_brawlstars_id", nullable = false, updatable = false)
    private long enemyBrawlerBrawlStarsId;

    @Column(name = "star_player_count", nullable = false)
    private int starPlayerCount;

    protected BrawlerBattleResultStatisticsCollectionEntity() {
    }

    public BrawlerBattleResultStatisticsCollectionEntity(
            long battleEventId,
            LocalDate battleDate,
            @Nullable SoloRankTierRange soloRankTierRange,
            @Nullable TrophyRange trophyRange,
            boolean duplicateBrawler,
            long brawlerBrawlStarsId,
            long enemyBrawlerBrawlStarsId
    ) {
        super(battleEventId, battleDate, soloRankTierRange, trophyRange, duplicateBrawler);
        this.brawlerBrawlStarsId = brawlerBrawlStarsId;
        this.enemyBrawlerBrawlStarsId = enemyBrawlerBrawlStarsId;
        this.starPlayerCount = 0;
    }

    public void starPlayer() {
        starPlayerCount++;
    }

    public Long getId() {
        return id;
    }

    public long getBrawlerBrawlStarsId() {
        return brawlerBrawlStarsId;
    }

    public long getEnemyBrawlerBrawlStarsId() {
        return enemyBrawlerBrawlStarsId;
    }

    public int getStarPlayerCount() {
        return starPlayerCount;
    }
}

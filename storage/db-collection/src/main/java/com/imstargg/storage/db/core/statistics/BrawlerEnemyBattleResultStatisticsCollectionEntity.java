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
@Table(name = "brawler_enemy_battle_result_stats_v3")
public class BrawlerEnemyBattleResultStatisticsCollectionEntity extends BrawlerBattleResultStatisticsBaseCollectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brawler_enemy_battle_result_stats_id")
    private Long id;

    @Column(name = "enemy_brawler_brawlstars_id", updatable = false, nullable = false)
    private long enemyBrawlerBrawlStarsId;

    protected BrawlerEnemyBattleResultStatisticsCollectionEntity() {
    }

    public BrawlerEnemyBattleResultStatisticsCollectionEntity(
            long eventBrawlStarsId,
            long brawlerBrawlStarsId,
            TrophyRange trophyRange,
            LocalDate battleDate,
            long enemyBrawlerBrawlStarsId
    ) {
        super(eventBrawlStarsId, brawlerBrawlStarsId, trophyRange, battleDate);
        this.enemyBrawlerBrawlStarsId = enemyBrawlerBrawlStarsId;
    }

    public BrawlerEnemyBattleResultStatisticsCollectionEntity(
            long eventBrawlStarsId,
            long brawlerBrawlStarsId,
            SoloRankTierRange soloRankTierRange,
            LocalDate battleDate,
            long enemyBrawlerBrawlStarsId
    ) {
        super(eventBrawlStarsId, brawlerBrawlStarsId, soloRankTierRange, battleDate);
        this.enemyBrawlerBrawlStarsId = enemyBrawlerBrawlStarsId;
    }

    public BrawlerEnemyBattleResultStatisticsCollectionEntity(
            long eventBrawlStarsId,
            long brawlerBrawlStarsId,
            LocalDate battleDate,
            long enemyBrawlerBrawlStarsId
    ) {
        super(eventBrawlStarsId, brawlerBrawlStarsId, battleDate);
        this.enemyBrawlerBrawlStarsId = enemyBrawlerBrawlStarsId;
    }

    public Long getId() {
        return id;
    }

    public long getEnemyBrawlerBrawlStarsId() {
        return enemyBrawlerBrawlStarsId;
    }
}

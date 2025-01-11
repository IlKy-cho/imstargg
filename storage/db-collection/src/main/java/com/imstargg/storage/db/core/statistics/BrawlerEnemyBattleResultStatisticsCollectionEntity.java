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
@Table(name = "brawler_enemy_battle_result_stats")
public class BrawlerEnemyBattleResultStatisticsCollectionEntity extends BrawlerBattleResultStatisticsBaseCollectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brawler_battle_result_stats_id")
    private Long id;

    @Column(name = "brawler_brawlstars_id", updatable = false, nullable = false)
    private long brawlerBrawlStarsId;

    @Column(name = "enemy_brawler_brawlstars_id", updatable = false, nullable = false)
    private long enemyBrawlerBrawlStarsId;

    protected BrawlerEnemyBattleResultStatisticsCollectionEntity() {
    }

    public BrawlerEnemyBattleResultStatisticsCollectionEntity(
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
}

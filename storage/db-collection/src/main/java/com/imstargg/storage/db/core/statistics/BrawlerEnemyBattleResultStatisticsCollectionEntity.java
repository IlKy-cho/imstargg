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

@Entity
@Table(name = "brawler_enemy_battle_result_stats_v2")
public class BrawlerEnemyBattleResultStatisticsCollectionEntity extends BrawlerBattleResultStatisticsBaseCollectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brawler_enemy_battle_result_stats_id")
    private Long id;

    @Column(name = "brawler_brawlstars_id", updatable = false, nullable = false)
    private long brawlerBrawlStarsId;

    @Column(name = "enemy_brawler_brawlstars_id", updatable = false, nullable = false)
    private long enemyBrawlerBrawlStarsId;

    protected BrawlerEnemyBattleResultStatisticsCollectionEntity() {
    }

    public BrawlerEnemyBattleResultStatisticsCollectionEntity(
            int seasonNumber,
            long battleEventId,
            @Nullable TrophyRange trophyRange,
            @Nullable SoloRankTierRange soloRankTierRange,
            long brawlerBrawlStarsId,
            long enemyBrawlerBrawlStarsId
    ) {
        super(seasonNumber, battleEventId, trophyRange, soloRankTierRange);
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

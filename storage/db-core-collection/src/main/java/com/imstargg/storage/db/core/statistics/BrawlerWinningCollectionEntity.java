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
@Table(name = "brawler_winning")
public class BrawlerWinningCollectionEntity extends BrawlerWinningBaseCollectionEntity {

    @Id
    @Column(name = "brawler_winning_id")
    private Long id;

    @Column(name = "enemy_brawler_brawlstars_id", updatable = false, nullable = false)
    private long enemyBrawlerBrawlStarsId;

    protected BrawlerWinningCollectionEntity() {
    }

    public BrawlerWinningCollectionEntity(
            @Nullable SoloRankTierRange soloRankTierRange,
            @Nullable TrophyRange trophyRange,
            long battleEventId,
            LocalDate battleDate,
            long brawlerBrawlStarsId,
            long enemyBrawlerBrawlStarsId
    ) {
        super(soloRankTierRange, trophyRange, battleEventId, battleDate, brawlerBrawlStarsId);
        this.enemyBrawlerBrawlStarsId = enemyBrawlerBrawlStarsId;
    }

    public Long getId() {
        return id;
    }

    public long getEnemyBrawlerBrawlStarsId() {
        return enemyBrawlerBrawlStarsId;
    }
}

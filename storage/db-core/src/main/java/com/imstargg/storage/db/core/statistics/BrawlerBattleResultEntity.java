package com.imstargg.storage.db.core.statistics;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "brawler_battle_result",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_event_battledate_brawler_enemybrawler_trophy_duplicate",
                        columnNames = {"event_brawlstars_id", "battle_date", "brawler_brawlstars_id", "enemy_brawler_brawlstars_id", "trophy_range", "duplicate_brawler"}
                ),
                @UniqueConstraint(
                        name = "uk_event_battledate_brawler_enemybrawler_ranktier_duplicate",
                        columnNames = {"event_brawlstars_id", "battle_date", "brawler_brawlstars_id", "enemy_brawler_brawlstars_id", "solo_rank_tier_range", "duplicate_brawler"}
                )
        }
)
public class BrawlerBattleResultEntity extends BrawlerBattleResultBaseEntity {

    @Id
    @Column(name = "brawler_battle_result_id")
    private Long id;

    @Column(name = "brawler_brawlstars_id", updatable = false, nullable = false)
    private long brawlerBrawlStarsId;

    @Column(name = "enemy_brawler_brawlstars_id", updatable = false, nullable = false)
    private long enemyBrawlerBrawlStarsId;

    @Column(name = "star_player_count", nullable = false)
    private int starPlayerCount;

    protected BrawlerBattleResultEntity() {
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

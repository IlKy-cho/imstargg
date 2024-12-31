package com.imstargg.storage.db.core.statistics;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "brawler_winning",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_brawler_event_battledate_trophy_duplicate",
                        columnNames = {"brawler_brawlstars_id", "event_brawlstars_id", "battle_date", "trophy_range", "duplicate_brawler"}
                ),
                @UniqueConstraint(
                        name = "uk_brawler_event_battledate_ranktier_duplicate",
                        columnNames = {"brawler_brawlstars_id", "event_brawlstars_id", "battle_date", "solo_rank_tier_range", "duplicate_brawler"}
                )
        }
)
public class BrawlerWinningEntity extends BrawlerWinningBaseEntity {

    @Id
    @Column(name = "brawler_winning_id")
    private Long id;

    @Column(name = "enemy_brawler_brawlstars_id", updatable = false, nullable = false)
    private long enemyBrawlerBrawlStarsId;

    @Column(name = "star_player_count", nullable = false)
    private int starPlayerCount;

    protected BrawlerWinningEntity() {
    }

}

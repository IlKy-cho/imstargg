package com.imstargg.storage.db.core.statistics;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(
        name = "brawler_winning",
        indexes = {
                @Index(
                        name = "ix__brawler_event_battledate",
                        columnList = "brawler_brawlstars_id, event_brawlstars_id, battle_date desc"
                ),
        }
)
public class BrawlerWinningEntity extends BrawlerWinningBaseEntity {

    @Id
    @Column(name = "brawler_winning_id")
    private Long id;

    @Column(name = "enemy_brawler_brawlstars_id", updatable = false, nullable = false)
    private long enemyBrawlerBrawlStarsId;

    protected BrawlerWinningEntity() {
    }

}

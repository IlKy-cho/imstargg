package com.imstargg.storage.db.core.statistics;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(
        name = "brawlers_winning",
        indexes = {
                @Index(
                        name = "ix__brawler_event_battledate",
                        columnList = "brawler_brawlstars_id, event_brawlstars_id, battle_date desc"
                ),
                @Index(
                        name = "ix___battledate_brawlerhash",
                        columnList = "battle_date desc, brawler_brawlstars_id_hash"
                )
        }
)
public class BrawlersWinningEntity extends BrawlerWinningBaseEntity {

    @Id
    @Column(name = "brawlers_winning_id")
    private Long id;

    @Column(name = "brawler_num", updatable = false, nullable = false)
    private int brawlerNum;

    @Column(name = "brawler_brawlstars_id_hash", length = 60, updatable = false, nullable = false)
    private String brawlerBrawlStarsIdHash;

    protected BrawlersWinningEntity() {
    }

}

package com.imstargg.storage.db.core.statistics;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "brawlers_winning",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_brawlerhash_event_battledate_trophy_duplicate",
                        columnNames = {"brawler_brawlstars_id_hash", "event_brawlstars_id", "battle_date", "trophy_range", "duplicate_brawler"}
                ),
                @UniqueConstraint(
                        name = "uk_brawlerhash_event_battledate_ranktier_duplicate",
                        columnNames = {"brawler_brawlstars_id_hash", "event_brawlstars_id", "battle_date", "solo_rank_tier_range", "duplicate_brawler"}
                )
        }
)
public class BrawlersWinningEntity extends BrawlerWinningBaseEntity {

    @Id
    @Column(name = "brawlers_winning_id")
    private Long id;

    @Embedded
    private BattleStatisticsEntityBrawlers brawlers;

    protected BrawlersWinningEntity() {
    }

}

package com.imstargg.storage.db.core.statistics;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "brawlers_battle_result",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_event_battledate_brawler_trophy_duplicate",
                        columnNames = {"event_brawlstars_id", "battle_date", "brawler_brawlstars_id", "brawler_brawlstars_id_hash", "trophy_range", "duplicate_brawler"}
                ),
                @UniqueConstraint(
                        name = "uk_event_battledate_brawler_ranktier_duplicate",
                        columnNames = {"event_brawlstars_id", "battle_date", "brawler_brawlstars_id", "brawler_brawlstars_id_hash", "solo_rank_tier_range", "duplicate_brawler"}
                )
        }
)
public class BrawlersBattleResultEntity extends BrawlerBattleResultBaseEntity {

    @Id
    @Column(name = "brawlers_winning_id")
    private Long id;

    @Embedded
    private BattleStatisticsEntityBrawlers brawlers;

    protected BrawlersBattleResultEntity() {
    }

}

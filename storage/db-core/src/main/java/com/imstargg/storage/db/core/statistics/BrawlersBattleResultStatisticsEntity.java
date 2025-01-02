package com.imstargg.storage.db.core.statistics;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "brawlers_battle_result_stats",
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
public class BrawlersBattleResultStatisticsEntity extends BrawlerBattleResultStatisticsBaseEntity {

    @Id
    @Column(name = "brawlers_battle_result_stats_id")
    private Long id;

    @Column(name = "brawler_brawlstars_id", updatable = false, nullable = false)
    private long brawlerBrawlStarsId;

    @Embedded
    private BattleStatisticsEntityBrawlers brawlers;

    protected BrawlersBattleResultStatisticsEntity() {
    }

    public Long getId() {
        return id;
    }

    public long getBrawlerBrawlStarsId() {
        return brawlerBrawlStarsId;
    }

    public BattleStatisticsEntityBrawlers getBrawlers() {
        return brawlers;
    }
}

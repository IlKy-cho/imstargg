package com.imstargg.storage.db.core.statistics;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "brawler_battle_result_stats",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_event_battledate_range_duplicate_brawler",
                        columnNames = {"event_brawlstars_id", "battle_date", "trophy_range", "solo_rank_tier_range", "duplicate_brawler", "brawler_brawlstars_id"}
                )
        }
)
public class BrawlerBattleResultStatisticsEntity extends BrawlerBattleResultStatisticsBaseEntity {

    @Id
    @Column(name = "brawler_battle_result_stats_id")
    private Long id;

    @Column(name = "brawler_brawlstars_id", updatable = false, nullable = false)
    private long brawlerBrawlStarsId;

    @Column(name = "star_player_count", nullable = false)
    private long starPlayerCount;

    protected BrawlerBattleResultStatisticsEntity() {
    }

    public Long getId() {
        return id;
    }

    public long getBrawlerBrawlStarsId() {
        return brawlerBrawlStarsId;
    }

    public long getStarPlayerCount() {
        return starPlayerCount;
    }
}

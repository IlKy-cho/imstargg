package com.imstargg.storage.db.core.statistics;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "brawler_battle_result_stats_v3",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_brawler_battle_result_stats__key",
                        columnNames = {"event_brawlstars_id", "brawler_brawlstars_id", "tier_range", "battle_date"}
                )
        },
        indexes = {
                @Index(
                        name = "ix_brawler_battle_result_stats__1",
                        columnList = "battle_date desc"
                ),
                @Index(
                        name = "ix_brawler_battle_result_stats__2",
                        columnList = "tier_range, battle_date desc"
                ),
                @Index(
                        name = "ix_brawler_battle_result_stats__3",
                        columnList = "brawler_brawlstars_id, tier_range, battle_date desc"
                )
        }
)
public class BrawlerBattleResultStatisticsEntity extends BrawlerBattleResultStatisticsBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brawler_battle_result_stats_id")
    private Long id;

    @Column(name = "star_player_count", nullable = false, updatable = false)
    private long starPlayerCount;

    protected BrawlerBattleResultStatisticsEntity() {
    }

    public Long getId() {
        return id;
    }

    public long getStarPlayerCount() {
        return starPlayerCount;
    }
}

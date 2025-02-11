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
        name = "brawler_battle_result_stats_v2",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_brawler_battle_result_stats__key",
                        columnNames = {"event_brawlstars_id", "battle_date", "trophy_range", "solo_rank_tier_range", "brawler_brawlstars_id"}
                )
        },
        indexes = {
                @Index(
                        name = "ix_brawler_battle_result_stats__1",
                        columnList = "battle_date desc, trophy_range, solo_rank_tier_range, brawler_brawlstars_id"
                )
        }
)
public class BrawlerBattleResultStatisticsEntity extends BrawlerBattleResultStatisticsBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

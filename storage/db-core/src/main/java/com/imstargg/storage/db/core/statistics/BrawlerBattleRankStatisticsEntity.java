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
        name = "brawler_battle_rank_stats_v3",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_brawler_battle_rank_stats__key",
                        columnNames = {"event_brawlstars_id", "brawler_brawlstars_id", "tier_range", "battle_date"}
                )
        },
        indexes = {
                @Index(
                        name = "ix_brawler_battle_rank_stats__1",
                        columnList = "battle_date desc"
                )
        }
)
public class BrawlerBattleRankStatisticsEntity extends BrawlerBattleRankStatisticsBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brawler_battle_rank_stats_id")
    private Long id;

    protected BrawlerBattleRankStatisticsEntity() {
    }

    public Long getId() {
        return id;
    }

}

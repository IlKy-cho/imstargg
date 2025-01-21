package com.imstargg.storage.db.core.statistics;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "brawlers_battle_result_stats",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_event_battledate_range_duplicate_brawler",
                        columnNames = {"event_brawlstars_id", "battle_date", "trophy_range", "solo_rank_tier_range", "duplicate_brawler", "brawler_brawlstars_id", "brawler_brawlstars_id_hash"}
                ),
        },
        indexes = {
                @Index(
                        name = "ix_battledate_range_brawler",
                        columnList = "battle_date desc, trophy_range, solo_rank_tier_range, brawler_brawlstars_id"
                )
        }
)
public class BrawlersBattleResultStatisticsEntity extends BrawlerBattleResultStatisticsBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

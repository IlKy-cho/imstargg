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
        name = "brawlers_battle_result_stats_v2",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_brawlers_battle_result_stats__key",
                        columnNames = {"season_number", "event_brawlstars_id", "trophy_range", "solo_rank_tier_range", "brawler_brawlstars_id", "brawler_brawlstars_id_hash"}
                ),
        },
        indexes = {
                @Index(
                        name = "ix_brawlers_battle_result_stats__1",
                        columnList = "season_number, trophy_range, solo_rank_tier_range, brawler_brawlstars_id, brawler_num"
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

package com.imstargg.storage.db.core.statistics;

import com.imstargg.core.enums.TrophyRange;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "brawlers_rank_stats",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_event_battledate_brawler_trophy_rank",
                        columnNames = {"event_brawlstars_id", "battle_date", "brawler_brawlstars_id", "brawler_brawlstars_id_hash", "trophy_range", "rank_value"}
                )
        }
)
public class BrawlersRankStatisticsEntity extends BrawlerBattleRankStatisticsBaseEntity {

    @Id
    @Column(name = "brawlers_rank_stats_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "trophy_range", length = 25, updatable = false, nullable = false)
    private TrophyRange trophyRange;

    @Column(name = "brawler_brawlstars_id", updatable = false, nullable = false)
    private long brawlerBrawlStarsId;

    @Embedded
    private BattleStatisticsEntityBrawlers brawlers;

    protected BrawlersRankStatisticsEntity() {
    }

    public Long getId() {
        return id;
    }

    public TrophyRange getTrophyRange() {
        return trophyRange;
    }

    public long getBrawlerBrawlStarsId() {
        return brawlerBrawlStarsId;
    }

    public BattleStatisticsEntityBrawlers getBrawlers() {
        return brawlers;
    }
}

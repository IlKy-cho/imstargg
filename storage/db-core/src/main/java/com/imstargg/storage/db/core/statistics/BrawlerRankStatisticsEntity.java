package com.imstargg.storage.db.core.statistics;

import com.imstargg.core.enums.TrophyRange;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "brawler_rank_stats",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_brawlerhash_event_battledate_trophy_rank",
                        columnNames = {"brawler_brawlstars_id_hash", "event_brawlstars_id", "battle_date", "trophy_range", "rank_value"}
                )
        }
)
public class BrawlerRankStatisticsEntity extends BattleStatisticsBaseEntity {

    @Id
    @Column(name = "brawler_rank_stats_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "trophy_range", length = 25, updatable = false, nullable = false)
    private TrophyRange trophyRange;

    @Column(name = "brawler_brawlstars_id", updatable = false, nullable = false)
    private long brawlerBrawlStarsId;

    @Column(name = "rank_value", updatable = false, nullable = false)
    private int rank;

    @Column(name = "rank_count", updatable = false, nullable = false)
    private int count;

    protected BrawlerRankStatisticsEntity() {
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

    public int getRank() {
        return rank;
    }

    public int getCount() {
        return count;
    }
}

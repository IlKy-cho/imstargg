package com.imstargg.storage.db.core.statistics;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(
        name = "brawler_rank",
        indexes = {
                @Index(
                        name = "ix_brawler_rank__brawlerbrawlstarsid_battleeventid_battledate",
                        columnList = "brawler_brawlstars_id, battle_event_id, battle_date desc"
                )
        }
)
public class BrawlerRankEntity extends BrawlerStatisticsBaseEntity {

    @Id
    @Column(name = "brawler_rank_id")
    private Long id;

    @Column(name = "rank", updatable = false, nullable = false)
    private int rank;

    @Column(name = "count", updatable = false, nullable = false)
    private int count;

    protected BrawlerRankEntity() {
    }

    public Long getId() {
        return id;
    }

    public int getRank() {
        return rank;
    }

    public int getCount() {
        return count;
    }
}

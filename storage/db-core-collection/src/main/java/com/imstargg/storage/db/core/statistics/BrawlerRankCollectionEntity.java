package com.imstargg.storage.db.core.statistics;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "brawler_rank")
public class BrawlerRankCollectionEntity extends BrawlerStatisticsBaseCollectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brawler_rank_id")
    private Long id;

    @Column(name = "rank", updatable = false, nullable = false)
    private int rank;

    @Column(name = "count", nullable = false)
    private int count;

    protected BrawlerRankCollectionEntity() {
    }

    public BrawlerRankCollectionEntity(
            long battleEventId,
            LocalDate battleDate,
            long brawlerBrawlStarsId,
            int rank
    ) {
        super(battleEventId, battleDate, brawlerBrawlStarsId);
        this.rank = rank;
    }

    public void countUp() {
        count++;
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

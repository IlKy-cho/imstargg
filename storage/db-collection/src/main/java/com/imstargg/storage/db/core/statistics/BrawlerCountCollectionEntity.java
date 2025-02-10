package com.imstargg.storage.db.core.statistics;

import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "brawler_count_v2")
public class BrawlerCountCollectionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brawler_count_id")
    private Long id;

    @Column(name = "brawler_brawlstars_id", updatable = false, nullable = false)
    private long brawlerBrawlStarsId;

    @Column(name = "trophy_range", columnDefinition = "varchar(25)", nullable = false, updatable = false)
    private TrophyRange trophyRange;

    @Column(name = "count_value", nullable = false)
    private int count;

    protected BrawlerCountCollectionEntity() {
    }

    public BrawlerCountCollectionEntity(long brawlerBrawlStarsId, TrophyRange trophyRange, int count) {
        this.brawlerBrawlStarsId = brawlerBrawlStarsId;
        this.trophyRange = trophyRange;
        this.count = count;
    }

    public void update(int count) {
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    public long getBrawlerBrawlStarsId() {
        return brawlerBrawlStarsId;
    }

    public TrophyRange getTrophyRange() {
        return trophyRange;
    }

    public int getCount() {
        return count;
    }
}

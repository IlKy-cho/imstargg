package com.imstargg.storage.db.core.statistics;

import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "brawler_item_count_v3")
public class BrawlerItemCountCollectionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brawler_item_count_id")
    private Long id;

    @Column(name = "brawler_brawlstars_id", updatable = false, nullable = false)
    private long brawlerBrawlStarsId;

    @Column(name = "item_brawlstars_id", updatable = false, nullable = false)
    private long itemBrawlStarsId;

    @Enumerated(EnumType.STRING)
    @Column(name = "trophy_range", columnDefinition = "varchar(25)", nullable = false, updatable = false)
    private TrophyRange trophyRange;

    @Column(name = "count_value", nullable = false)
    private int count;

    protected BrawlerItemCountCollectionEntity() {
    }

    public BrawlerItemCountCollectionEntity(
            long brawlerBrawlStarsId,
            long itemBrawlStarsId,
            TrophyRange trophyRange,
            int count
    ) {
        this.brawlerBrawlStarsId = brawlerBrawlStarsId;
        this.itemBrawlStarsId = itemBrawlStarsId;
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

    public long getItemBrawlStarsId() {
        return itemBrawlStarsId;
    }

    public TrophyRange getTrophyRange() {
        return trophyRange;
    }

    public int getCount() {
        return count;
    }
}

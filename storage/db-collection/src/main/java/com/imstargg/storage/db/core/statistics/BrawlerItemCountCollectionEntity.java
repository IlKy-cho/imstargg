package com.imstargg.storage.db.core.statistics;

import com.imstargg.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "brawler_item_count")
public class BrawlerItemCountCollectionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "brawler_brawlstars_id", updatable = false, nullable = false)
    private long brawlerBrawlStarsId;

    @Column(name = "item_brawlstars_id", updatable = false, nullable = false)
    private long itemBrawlStarsId;

    @Column(name = "item_count")
    private int count;

    protected BrawlerItemCountCollectionEntity() {
    }

    public BrawlerItemCountCollectionEntity(long brawlerBrawlStarsId, long itemBrawlStarsId, int count) {
        this.brawlerBrawlStarsId = brawlerBrawlStarsId;
        this.itemBrawlStarsId = itemBrawlStarsId;
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

    public int getCount() {
        return count;
    }
}

package com.imstargg.storage.db.core.statistics;

import com.imstargg.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "brawler_item_count",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_brawler_item_count", columnNames = {"brawler_brawlstars_id", "item_brawlstars_id"}
                )
        }
)
public class BrawlerItemCountEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brawler_item_count_id")
    private Long id;

    @Column(name = "brawler_brawlstars_id", updatable = false, nullable = false)
    private long brawlerBrawlStarsId;

    @Column(name = "item_brawlstars_id", updatable = false, nullable = false)
    private long itemBrawlStarsId;

    @Column(name = "count_value", nullable = false)
    private int count;

    protected BrawlerItemCountEntity() {
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

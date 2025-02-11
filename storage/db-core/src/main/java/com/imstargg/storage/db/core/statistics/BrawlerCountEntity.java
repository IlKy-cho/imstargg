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
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "brawler_count_v2",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_brawler_count", columnNames = {"brawler_brawlstars_id", "trophy_range"}
                )
        }
)
public class BrawlerCountEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brawler_count_id")
    private Long id;

    @Column(name = "brawler_brawlstars_id", updatable = false, nullable = false)
    private long brawlerBrawlStarsId;

    @Enumerated(EnumType.STRING)
    @Column(name = "trophy_range", columnDefinition = "varchar(25)", nullable = false, updatable = false)
    private TrophyRange trophyRange;

    @Column(name = "count_value", nullable = false, updatable = false)
    private int count;

    protected BrawlerCountEntity() {
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

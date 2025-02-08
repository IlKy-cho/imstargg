package com.imstargg.storage.db.core.ranking;

import com.imstargg.core.enums.Country;
import com.imstargg.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "brawler_ranking")
public class BrawlerRankingCollectionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brawler_ranking_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "country", columnDefinition = "varchar(25)", nullable = false, updatable = false)
    private Country country;

    @Column(name = "brawler_brawlstars_id", nullable = false, updatable = false)
    private long brawlerBrawlStarsId;

    @Embedded
    private RankingEntityPlayer player;

    @Column(name = "trophies", nullable = false)
    private int trophies;

    @Column(name = "rank_value", nullable = false, updatable = false)
    private int rank;

    protected BrawlerRankingCollectionEntity() {
    }

    public BrawlerRankingCollectionEntity(
            Country country,
            long brawlerBrawlStarsId,
            RankingEntityPlayer player,
            int trophies,
            int rank
    ) {
        this.country = country;
        this.brawlerBrawlStarsId = brawlerBrawlStarsId;
        this.player = player;
        this.trophies = trophies;
        this.rank = rank;
    }

    public void update(RankingEntityPlayer player, int trophies) {
        this.player = player;
        this.trophies = trophies;
    }

    public Long getId() {
        return id;
    }

    public Country getCountry() {
        return country;
    }

    public long getBrawlerBrawlStarsId() {
        return brawlerBrawlStarsId;
    }

    public RankingEntityPlayer getPlayer() {
        return player;
    }

    public int getTrophies() {
        return trophies;
    }

    public int getRank() {
        return rank;
    }
}

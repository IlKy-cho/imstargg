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
@Table(name = "player_ranking")
public class PlayerRankingCollectionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_ranking_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "country", columnDefinition = "varchar(25)", nullable = false)
    private Country country;

    @Embedded
    private RankingEntityPlayer player;

    @Column(name = "trophies", nullable = false, updatable = false)
    private int trophies;

    @Column(name = "rank_value", nullable = false, updatable = false)
    private int rank;

    protected PlayerRankingCollectionEntity() {
    }

    public PlayerRankingCollectionEntity(
            Country country,
            RankingEntityPlayer player,
            int trophies,
            int rank
    ) {
        this.country = country;
        this.player = player;
        this.trophies = trophies;
        this.rank = rank;
    }

    public Long getId() {
        return id;
    }

    public Country getCountry() {
        return country;
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

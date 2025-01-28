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
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "player_ranking",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_player_ranking__country_rank", columnNames = {"country", "rank_value"})
        }
)
public class PlayerRankingEntity extends BaseEntity {

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

    protected PlayerRankingEntity() {
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

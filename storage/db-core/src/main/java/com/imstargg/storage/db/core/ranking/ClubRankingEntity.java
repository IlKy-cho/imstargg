package com.imstargg.storage.db.core.ranking;

import com.imstargg.core.enums.Country;
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
        name = "club_ranking",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_club_ranking__country_rank",
                        columnNames = {"country", "rank_value"}
                )
        }
)
public class ClubRankingEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_ranking_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "country", columnDefinition = "varchar(25)", nullable = false, updatable = false)
    private Country country;

    @Column(name = "brawlstars_tag", length = 45, updatable = false, nullable = false)
    private String brawlStarsTag;

    @Column(name = "name", length = 45, nullable = false, updatable = false)
    private String name;

    @Column(name = "badge_brawlstars_id", nullable = false, updatable = false)
    private long badgeBrawlStarsId;

    @Column(name = "trophies", nullable = false, updatable = false)
    private int trophies;

    @Column(name = "rank_value", nullable = false, updatable = false)
    private int rank;

    @Column(name = "member_count", nullable = false, updatable = false)
    private int memberCount;

    protected ClubRankingEntity() {
    }

    public Long getId() {
        return id;
    }

    public Country getCountry() {
        return country;
    }

    public String getBrawlStarsTag() {
        return brawlStarsTag;
    }

    public String getName() {
        return name;
    }

    public long getBadgeBrawlStarsId() {
        return badgeBrawlStarsId;
    }

    public int getTrophies() {
        return trophies;
    }

    public int getRank() {
        return rank;
    }

    public int getMemberCount() {
        return memberCount;
    }
}

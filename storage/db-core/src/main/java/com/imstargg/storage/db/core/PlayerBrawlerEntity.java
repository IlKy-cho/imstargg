package com.imstargg.storage.db.core;

import com.imstargg.storage.db.support.LongListToStringConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.List;

@Entity
@Table(
        name = "player_brawler",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_playerbrawler__playerid_brawlerbrawlstarsid",
                        columnNames = {"player_id", "brawler_brawlstars_id"}
                )
        }
)
public class PlayerBrawlerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_brawler_id")
    private Long id;

    @Column(name = "player_id", updatable = false, nullable = false)
    private long playerId;

    @Column(name = "brawler_brawlstars_id", updatable = false, nullable = false)
    private long brawlerBrawlStarsId;

    @Column(name = "power", nullable = false, updatable = false)
    private int power;

    @Column(name = "trophy_rank", nullable = false, updatable = false)
    private int rank;

    @Column(name = "trophies", nullable = false, updatable = false)
    private int trophies;

    @Column(name = "highest_trophies", nullable = false, updatable = false)
    private int highestTrophies;

    @Convert(converter = LongListToStringConverter.class)
    @Column(name = "gear_brawlstars_ids", columnDefinition = "varchar(255)", nullable = false, updatable = false)
    private List<Long> gearBrawlStarsIds;

    @Convert(converter = LongListToStringConverter.class)
    @Column(name = "star_power_brawlstars_ids", columnDefinition = "varchar(255)", nullable = false, updatable = false)
    private List<Long> starPowerBrawlStarsIds;

    @Convert(converter = LongListToStringConverter.class)
    @Column(name = "gadget_brawlstars_ids", columnDefinition = "varchar(255)", nullable = false, updatable = false)
    private List<Long> gadgetBrawlStarsIds;

    protected PlayerBrawlerEntity() {
    }

    public Long getId() {
        return id;
    }

    public long getPlayerId() {
        return playerId;
    }

    public long getBrawlerBrawlStarsId() {
        return brawlerBrawlStarsId;
    }

    public int getPower() {
        return power;
    }

    public int getRank() {
        return rank;
    }

    public int getTrophies() {
        return trophies;
    }

    public int getHighestTrophies() {
        return highestTrophies;
    }

    public List<Long> getGearBrawlStarsIds() {
        return gearBrawlStarsIds;
    }

    public List<Long> getStarPowerBrawlStarsIds() {
        return starPowerBrawlStarsIds;
    }

    public List<Long> getGadgetBrawlStarsIds() {
        return gadgetBrawlStarsIds;
    }
}

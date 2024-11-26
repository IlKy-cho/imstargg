package com.imstargg.storage.db.core;

import com.imstargg.storage.db.support.LongListToStringConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "player_brawler")
public class PlayerBrawlerCollectionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_brawler_id")
    private Long id;

    @Column(name = "player_id", updatable = false, nullable = false)
    private long playerId;

    @Column(name = "brawler_brawlstars_id", updatable = false, nullable = false)
    private long brawlerBrawlStarsId;

    @Column(name = "power", nullable = false)
    private int power;

    @Column(name = "trophy_rank", nullable = false)
    private int rank;

    @Column(name = "trophies", nullable = false)
    private int trophies;

    @Column(name = "highest_trophies", nullable = false)
    private int highestTrophies;

    @Convert(converter = LongListToStringConverter.class)
    @Column(name = "gear_brawlstars_ids", columnDefinition = "varchar(255)", nullable = false)
    private List<Long> gearBrawlStarsIds;

    @Convert(converter = LongListToStringConverter.class)
    @Column(name = "star_power_brawlstars_ids", columnDefinition = "varchar(255)", nullable = false)
    private List<Long> starPowerBrawlStarsIds;

    @Convert(converter = LongListToStringConverter.class)
    @Column(name = "gadget_brawlstars_ids", columnDefinition = "varchar(255)", nullable = false)
    private List<Long> gadgetBrawlStarsIds;

    protected PlayerBrawlerCollectionEntity() {
    }

    public PlayerBrawlerCollectionEntity(
            long playerId,
            long brawlerId,
            int power,
            int rank,
            int trophies,
            int highestTrophies,
            List<Long> gearBrawlStarsIds,
            List<Long> starPowerBrawlStarsIds,
            List<Long> gadgetBrawlStarsIds
    ) {
        this.playerId = playerId;
        this.brawlerBrawlStarsId = brawlerId;
        this.power = power;
        this.rank = rank;
        this.trophies = trophies;
        this.highestTrophies = highestTrophies;
        this.gearBrawlStarsIds = gearBrawlStarsIds;
        this.starPowerBrawlStarsIds = starPowerBrawlStarsIds;
        this.gadgetBrawlStarsIds = gadgetBrawlStarsIds;
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

package com.imstargg.storage.db.core;

import com.imstargg.storage.db.support.LongListToStringConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "player_brawler")
public class PlayerBrawlerCollectionEntity {

    @Id
    @Column(name = "player_brawler_id")
    private Long id;

    @Column(name = "player_id", updatable = false, nullable = false)
    private long playerId;

    @Column(name = "brawler_id", updatable = false, nullable = false)
    private long brawlerId;

    @Column(name = "power", nullable = false)
    private int power;

    @Column(name = "trophy_rank", nullable = false)
    private int rank;

    @Column(name = "trophies", nullable = false)
    private int trophies;

    @Column(name = "highest_trophies", nullable = false)
    private int highestTrophies;

    @Convert(converter = LongListToStringConverter.class)
    @Column(name = "gear_ids", columnDefinition = "varchar(255)", nullable = false)
    private List<Long> gearIds;

    @Convert(converter = LongListToStringConverter.class)
    @Column(name = "star_power_ids", columnDefinition = "varchar(255)", nullable = false)
    private List<Long> starPowerIds;

    @Convert(converter = LongListToStringConverter.class)
    @Column(name = "gadget_ids", columnDefinition = "varchar(255)", nullable = false)
    private List<Long> gadgetIds;

    protected PlayerBrawlerCollectionEntity() {
    }

    public PlayerBrawlerCollectionEntity(
            long playerId,
            long brawlerId,
            int power,
            int rank,
            int trophies,
            int highestTrophies,
            List<Long> gearIds,
            List<Long> starPowerIds,
            List<Long> gadgetIds
    ) {
        this.playerId = playerId;
        this.brawlerId = brawlerId;
        this.power = power;
        this.rank = rank;
        this.trophies = trophies;
        this.highestTrophies = highestTrophies;
        this.gearIds = gearIds;
        this.starPowerIds = starPowerIds;
        this.gadgetIds = gadgetIds;
    }

    public Long getId() {
        return id;
    }

    public long getPlayerId() {
        return playerId;
    }

    public long getBrawlerId() {
        return brawlerId;
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

    public List<Long> getGearIds() {
        return gearIds;
    }

    public List<Long> getStarPowerIds() {
        return starPowerIds;
    }

    public List<Long> getGadgetIds() {
        return gadgetIds;
    }
}

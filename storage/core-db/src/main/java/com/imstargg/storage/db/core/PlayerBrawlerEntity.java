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
                        name = "uk_playerbrawler__playerid_brawlerid",
                        columnNames = {"player_id", "brawler_id"}
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

    protected PlayerBrawlerEntity() {
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

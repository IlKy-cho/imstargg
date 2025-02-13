package com.imstargg.storage.db.core;

import com.imstargg.storage.db.support.LongListToStringConverter;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "player_brawler")
public class PlayerBrawlerCollectionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_brawler_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player_id", updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private PlayerCollectionEntity player;

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
            PlayerCollectionEntity player,
            long brawlerBrawlStarsId,
            int power,
            int rank,
            int trophies,
            int highestTrophies,
            List<Long> gearBrawlStarsIds,
            List<Long> starPowerBrawlStarsIds,
            List<Long> gadgetBrawlStarsIds
    ) {
        this.player = player;
        this.brawlerBrawlStarsId = brawlerBrawlStarsId;
        this.power = power;
        this.rank = rank;
        this.trophies = trophies;
        this.highestTrophies = highestTrophies;
        this.gearBrawlStarsIds = gearBrawlStarsIds;
        this.starPowerBrawlStarsIds = starPowerBrawlStarsIds;
        this.gadgetBrawlStarsIds = gadgetBrawlStarsIds;
    }

    public boolean update(
            int power,
            int rank,
            int trophies,
            int highestTrophies,
            List<Long> gearBrawlStarsIds,
            List<Long> starPowerBrawlStarsIds,
            List<Long> gadgetBrawlStarsIds
    ) {
        boolean updated = power != this.power
                || rank != this.rank
                || trophies != this.trophies
                || highestTrophies != this.highestTrophies
                || !gearBrawlStarsIds.equals(this.gearBrawlStarsIds)
                || !starPowerBrawlStarsIds.equals(this.starPowerBrawlStarsIds)
                || !gadgetBrawlStarsIds.equals(this.gadgetBrawlStarsIds);
        this.power = power;
        this.rank = rank;
        this.trophies = trophies;
        this.highestTrophies = highestTrophies;
        this.gearBrawlStarsIds = gearBrawlStarsIds;
        this.starPowerBrawlStarsIds = starPowerBrawlStarsIds;
        this.gadgetBrawlStarsIds = gadgetBrawlStarsIds;
        return updated;
    }

    public void handleError() {
        if (power < 1 || power > 11) {
            // swap power and trophies
            int temp = power;
            power = trophies;
            trophies = temp;

            // swap rank and highestTrophies
            temp = rank;
            rank = highestTrophies;
            highestTrophies = temp;
        }
    }

    public Long getId() {
        return id;
    }

    public PlayerCollectionEntity getPlayer() {
        return player;
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

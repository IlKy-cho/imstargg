package com.imstargg.storage.db.core;

import com.imstargg.core.enums.PlayerStatus;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "player",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_player__tag", columnNames = "tag")
        },
        indexes = {
                @Index(name = "ix_player__name", columnList = "name")
        }
)
public class PlayerEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private Long id;

    @Column(name = "brawl_stars_tag", length = 45, updatable = false, nullable = false)
    private String brawlStarsTag;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(45)", nullable = false)
    private PlayerStatus status;

    @Column(name = "name", length = 105, nullable = false)
    private String name;

    @Column(name = "name_color", length = 45, nullable = false)
    private String nameColor;

    @Column(name = "icon_id", nullable = false)
    private long iconId;

    @Column(name = "trophies", nullable = false)
    private int trophies;

    @Column(name = "highest_trophies", nullable = false)
    private int highestTrophies;

    @Nullable
    @Column(name = "club_tag", length = 45)
    private String clubTag;

    protected PlayerEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getBrawlStarsTag() {
        return brawlStarsTag;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getNameColor() {
        return nameColor;
    }

    public long getIconId() {
        return iconId;
    }

    public int getTrophies() {
        return trophies;
    }

    public int getHighestTrophies() {
        return highestTrophies;
    }

    @Nullable
    public String getClubTag() {
        return clubTag;
    }
}

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
import jakarta.persistence.Table;

@Entity
@Table(name = "player")
public class PlayerCollectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(45)", nullable = false)
    private PlayerStatus status;

    @Column(name = "tag", length = 45, updatable = false, nullable = false)
    private String tag;

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

    @Column(name = "exp_level", nullable = false)
    private int expLevel;

    @Column(name = "exp_points", nullable = false)
    private int expPoints;

    @Column(name = "qualified_from_championship_challenge", nullable = false)
    private boolean qualifiedFromChampionshipChallenge;

    @Column(name = "victories_3vs3", nullable = false)
    private int victories3vs3;

    @Column(name = "solo_victories", nullable = false)
    private int soloVictories;

    @Column(name = "duo_victories", nullable = false)
    private int duoVictories;

    @Column(name = "best_robo_rumble_time", nullable = false)
    private int bestRoboRumbleTime;

    @Column(name = "best_time_as_big_brawler", nullable = false)
    private int bestTimeAsBigBrawler;

    @Nullable
    @Column(name = "club_tag", length = 45)
    private String clubTag;

    protected PlayerCollectionEntity() {
    }

    public PlayerCollectionEntity(
            PlayerStatus status,
            String tag,
            String name,
            String nameColor,
            long iconId,
            int trophies,
            int highestTrophies,
            int expLevel,
            int expPoints,
            boolean qualifiedFromChampionshipChallenge,
            int victories3vs3,
            int soloVictories,
            int duoVictories,
            int bestRoboRumbleTime,
            int bestTimeAsBigBrawler,
            @Nullable String clubTag
    ) {
        this.status = status;
        this.tag = tag;
        this.name = name;
        this.nameColor = nameColor;
        this.iconId = iconId;
        this.trophies = trophies;
        this.highestTrophies = highestTrophies;
        this.expLevel = expLevel;
        this.expPoints = expPoints;
        this.qualifiedFromChampionshipChallenge = qualifiedFromChampionshipChallenge;
        this.victories3vs3 = victories3vs3;
        this.soloVictories = soloVictories;
        this.duoVictories = duoVictories;
        this.bestRoboRumbleTime = bestRoboRumbleTime;
        this.bestTimeAsBigBrawler = bestTimeAsBigBrawler;
        this.clubTag = clubTag;
    }
}

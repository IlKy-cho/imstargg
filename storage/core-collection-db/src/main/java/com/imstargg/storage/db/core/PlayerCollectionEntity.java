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
public class PlayerCollectionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private Long id;

    @Column(name = "brawlstars_tag", length = 45, updatable = false, nullable = false)
    private String brawlStarsTag;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(45)", nullable = false)
    private PlayerStatus status;

    @Column(name = "name", length = 105, nullable = false)
    private String name;

    @Column(name = "name_color", length = 45, nullable = false)
    private String nameColor;

    @Column(name = "icon_brawlstars_id", nullable = false)
    private long iconBrawlStarsId;

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
    @Column(name = "brawlstars_club_tag", length = 45, updatable = false, nullable = false)
    private String brawlStarsClubTag;

    @Column(name = "update_weight", nullable = false)
    private long updateWeight;

    protected PlayerCollectionEntity() {
    }

    public PlayerCollectionEntity(
            PlayerStatus status,
            String brawlStarsTag,
            String name,
            String nameColor,
            long iconBrawlStarsId,
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
            @Nullable String brawlStarsClubTag
    ) {
        this.status = status;
        this.brawlStarsTag = brawlStarsTag;
        this.name = name;
        this.nameColor = nameColor;
        this.iconBrawlStarsId = iconBrawlStarsId;
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
        this.brawlStarsClubTag = brawlStarsClubTag;
    }

    public Long getId() {
        return id;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public String getBrawlStarsTag() {
        return brawlStarsTag;
    }

    public String getName() {
        return name;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNameColor(String nameColor) {
        this.nameColor = nameColor;
    }

    public void setIconBrawlStarsId(long iconBrawlStarsId) {
        this.iconBrawlStarsId = iconBrawlStarsId;
    }

    public void setTrophies(int trophies) {
        this.trophies = trophies;
    }

    public void setHighestTrophies(int highestTrophies) {
        this.highestTrophies = highestTrophies;
    }

    public void setExpLevel(int expLevel) {
        this.expLevel = expLevel;
    }

    public void setExpPoints(int expPoints) {
        this.expPoints = expPoints;
    }

    public void setQualifiedFromChampionshipChallenge(boolean qualifiedFromChampionshipChallenge) {
        this.qualifiedFromChampionshipChallenge = qualifiedFromChampionshipChallenge;
    }

    public void setVictories3vs3(int victories3vs3) {
        this.victories3vs3 = victories3vs3;
    }

    public void setSoloVictories(int soloVictories) {
        this.soloVictories = soloVictories;
    }

    public void setDuoVictories(int duoVictories) {
        this.duoVictories = duoVictories;
    }

    public void setBestRoboRumbleTime(int bestRoboRumbleTime) {
        this.bestRoboRumbleTime = bestRoboRumbleTime;
    }

    public void setBestTimeAsBigBrawler(int bestTimeAsBigBrawler) {
        this.bestTimeAsBigBrawler = bestTimeAsBigBrawler;
    }

    public void setBrawlStarsClubTag(@Nullable String brawlStarsClubTag) {
        this.brawlStarsClubTag = brawlStarsClubTag;
    }
}

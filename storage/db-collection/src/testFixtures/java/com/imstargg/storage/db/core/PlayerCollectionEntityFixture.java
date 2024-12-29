package com.imstargg.storage.db.core;

import com.imstargg.test.java.LongIncrementUtil;
import jakarta.annotation.Nullable;

public class PlayerCollectionEntityFixture {

    @Nullable
    private String brawlStarsTag;

    @Nullable
    private String name;

    @Nullable
    private String nameColor;

    private long iconBrawlStarsId;

    private int trophies;

    private int highestTrophies;

    private int expLevel;

    private int expPoints;

    private boolean qualifiedFromChampionshipChallenge;

    private int victories3vs3;

    private int soloVictories;

    private int duoVictories;

    private int bestRoboRumbleTime;

    private int bestTimeAsBigBrawler;

    @Nullable
    private String brawlStarsClubTag;

    public PlayerCollectionEntityFixture brawlStarsTag(String brawlStarsTag) {
        this.brawlStarsTag = brawlStarsTag;
        return this;
    }

    public PlayerCollectionEntityFixture name(String name) {
        this.name = name;
        return this;
    }

    public PlayerCollectionEntityFixture nameColor(String nameColor) {
        this.nameColor = nameColor;
        return this;
    }

    public PlayerCollectionEntityFixture iconBrawlStarsId(long iconBrawlStarsId) {
        this.iconBrawlStarsId = iconBrawlStarsId;
        return this;
    }

    public PlayerCollectionEntityFixture trophies(int trophies) {
        this.trophies = trophies;
        return this;
    }

    public PlayerCollectionEntityFixture highestTrophies(int highestTrophies) {
        this.highestTrophies = highestTrophies;
        return this;
    }

    public PlayerCollectionEntityFixture expLevel(int expLevel) {
        this.expLevel = expLevel;
        return this;
    }

    public PlayerCollectionEntityFixture expPoints(int expPoints) {
        this.expPoints = expPoints;
        return this;
    }

    public PlayerCollectionEntityFixture qualifiedFromChampionshipChallenge(boolean qualifiedFromChampionshipChallenge) {
        this.qualifiedFromChampionshipChallenge = qualifiedFromChampionshipChallenge;
        return this;
    }

    public PlayerCollectionEntityFixture victories3vs3(int victories3vs3) {
        this.victories3vs3 = victories3vs3;
        return this;
    }

    public PlayerCollectionEntityFixture soloVictories(int soloVictories) {
        this.soloVictories = soloVictories;
        return this;
    }

    public PlayerCollectionEntityFixture duoVictories(int duoVictories) {
        this.duoVictories = duoVictories;
        return this;
    }

    public PlayerCollectionEntityFixture bestRoboRumbleTime(int bestRoboRumbleTime) {
        this.bestRoboRumbleTime = bestRoboRumbleTime;
        return this;
    }

    public PlayerCollectionEntityFixture bestTimeAsBigBrawler(int bestTimeAsBigBrawler) {
        this.bestTimeAsBigBrawler = bestTimeAsBigBrawler;
        return this;
    }

    public PlayerCollectionEntityFixture brawlStarsClubTag(String brawlStarsClubTag) {
        this.brawlStarsClubTag = brawlStarsClubTag;
        return this;
    }

    public PlayerCollectionEntity build() {
        fillRequiredFields();
        return new PlayerCollectionEntity(
            brawlStarsTag,
            name,
            nameColor,
            iconBrawlStarsId,
            trophies,
            highestTrophies,
            expLevel,
            expPoints,
            qualifiedFromChampionshipChallenge,
            victories3vs3,
            soloVictories,
            duoVictories,
            bestRoboRumbleTime,
            bestTimeAsBigBrawler,
            brawlStarsClubTag
        );
    }

    private void fillRequiredFields() {
        if (brawlStarsTag == null) {
            brawlStarsTag = "brawlStarsTag-" + LongIncrementUtil.next();
        }
        if (name == null) {
            name = "name-" + LongIncrementUtil.next();
        }
        if (nameColor == null) {
            nameColor = "nameColor-" + LongIncrementUtil.next();
        }
        if (brawlStarsClubTag == null) {
            brawlStarsClubTag = "brawlStarsClubTag-" + LongIncrementUtil.next();
        }
    }
}

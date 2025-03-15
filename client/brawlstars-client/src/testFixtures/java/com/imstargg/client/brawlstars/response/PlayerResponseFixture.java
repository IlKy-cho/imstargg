package com.imstargg.client.brawlstars.response;

import com.imstargg.test.java.IntegerIncrementUtil;
import com.imstargg.test.java.LongIncrementUtil;

import java.util.List;
import java.util.stream.IntStream;

public class PlayerResponseFixture {

    private String tag = "#TAG" + LongIncrementUtil.next();
    private String name = "player" + LongIncrementUtil.next();
    private String nameColor = "nameColor" + LongIncrementUtil.next();
    private PlayerIconResponse icon = new PlayerIconResponse(LongIncrementUtil.next());
    private int trophies = IntegerIncrementUtil.next();
    private int highestTrophies = IntegerIncrementUtil.next();
    private int expLevel = IntegerIncrementUtil.next();
    private int expPoints = IntegerIncrementUtil.next();
    private boolean isQualifiedFromChampionshipChallenge = IntegerIncrementUtil.next() % 2 == 0;
    private int victories3vs3 = IntegerIncrementUtil.next();
    private int soloVictories = IntegerIncrementUtil.next();
    private int duoVictories = IntegerIncrementUtil.next();
    private int bestRoboRumbleTime = IntegerIncrementUtil.next();
    private int bestTimeAsBigBrawler = IntegerIncrementUtil.next();
    private PlayerClubResponse club = new PlayerClubResponseFixture().build();
    private List<BrawlerStatResponse> brawlers = IntStream.range(1, IntegerIncrementUtil.next(10))
            .mapToObj(i -> new BrawlerStatResponseFixture().build())
            .toList();

    public PlayerResponseFixture tag(String tag) {
        this.tag = tag;
        return this;
    }

    public PlayerResponseFixture name(String name) {
        this.name = name;
        return this;
    }

    public PlayerResponseFixture nameColor(String nameColor) {
        this.nameColor = nameColor;
        return this;
    }

    public PlayerResponseFixture icon(PlayerIconResponse icon) {
        this.icon = icon;
        return this;
    }

    public PlayerResponseFixture trophies(int trophies) {
        this.trophies = trophies;
        return this;
    }

    public PlayerResponseFixture highestTrophies(int highestTrophies) {
        this.highestTrophies = highestTrophies;
        return this;
    }

    public PlayerResponseFixture expLevel(int expLevel) {
        this.expLevel = expLevel;
        return this;
    }

    public PlayerResponseFixture expPoints(int expPoints) {
        this.expPoints = expPoints;
        return this;
    }

    public PlayerResponseFixture isQualifiedFromChampionshipChallenge(boolean isQualifiedFromChampionshipChallenge) {
        this.isQualifiedFromChampionshipChallenge = isQualifiedFromChampionshipChallenge;
        return this;
    }

    public PlayerResponseFixture victories3vs3(int victories3vs3) {
        this.victories3vs3 = victories3vs3;
        return this;
    }

    public PlayerResponseFixture soloVictories(int soloVictories) {
        this.soloVictories = soloVictories;
        return this;
    }

    public PlayerResponseFixture duoVictories(int duoVictories) {
        this.duoVictories = duoVictories;
        return this;
    }

    public PlayerResponseFixture bestRoboRumbleTime(int bestRoboRumbleTime) {
        this.bestRoboRumbleTime = bestRoboRumbleTime;
        return this;
    }

    public PlayerResponseFixture bestTimeAsBigBrawler(int bestTimeAsBigBrawler) {
        this.bestTimeAsBigBrawler = bestTimeAsBigBrawler;
        return this;
    }

    public PlayerResponseFixture club(PlayerClubResponse club) {
        this.club = club;
        return this;
    }

    public PlayerResponseFixture brawlers(List<BrawlerStatResponse> brawlers) {
        this.brawlers = brawlers;
        return this;
    }

    public PlayerResponse build() {
        return new PlayerResponse(
                tag,
                name,
                nameColor,
                icon,
                trophies,
                highestTrophies,
                expLevel,
                expPoints,
                isQualifiedFromChampionshipChallenge,
                victories3vs3,
                soloVictories,
                duoVictories,
                bestRoboRumbleTime,
                bestTimeAsBigBrawler,
                club,
                brawlers
        );
    }
}

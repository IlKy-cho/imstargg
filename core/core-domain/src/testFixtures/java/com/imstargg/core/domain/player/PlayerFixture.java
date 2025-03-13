package com.imstargg.core.domain.player;

import com.imstargg.core.domain.BrawlStarsTag;
import com.imstargg.core.enums.PlayerStatus;
import com.imstargg.core.enums.SoloRankTier;
import com.imstargg.test.java.IntegerIncrementUtil;
import com.imstargg.test.java.LongIncrementUtil;

import javax.annotation.Nullable;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerFixture {

    private BrawlStarsTag tag = new BrawlStarsTag("#TAG" + LongIncrementUtil.next());
    private String name = "Player" + LongIncrementUtil.next();
    private String nameColor = "nameColor" + LongIncrementUtil.next();
    private long iconId = LongIncrementUtil.next();
    private int trophies = ThreadLocalRandom.current().nextInt(0, 100000);
    private int highestTrophies = trophies;
    @Nullable
    private SoloRankTier soloRankTier = SoloRankTier.values()[IntegerIncrementUtil.next(SoloRankTier.values().length)];
    @Nullable
    private BrawlStarsTag clubTag = new BrawlStarsTag("CLUB" + LongIncrementUtil.next());
    private OffsetDateTime updatedAt = OffsetDateTime.now(Clock.systemUTC());
    private PlayerStatus status = PlayerStatus.values()[IntegerIncrementUtil.next(PlayerStatus.values().length)];

    public PlayerFixture tag(BrawlStarsTag tag) {
        this.tag = tag;
        return this;
    }

    public PlayerFixture name(String name) {
        this.name = name;
        return this;
    }

    public PlayerFixture nameColor(String nameColor) {
        this.nameColor = nameColor;
        return this;
    }

    public PlayerFixture iconId(long iconId) {
        this.iconId = iconId;
        return this;
    }

    public PlayerFixture trophies(int trophies) {
        this.trophies = trophies;
        return this;
    }

    public PlayerFixture highestTrophies(int highestTrophies) {
        this.highestTrophies = highestTrophies;
        return this;
    }

    public PlayerFixture soloRankTier(@Nullable SoloRankTier soloRankTier) {
        this.soloRankTier = soloRankTier;
        return this;
    }

    public PlayerFixture clubTag(@Nullable BrawlStarsTag clubTag) {
        this.clubTag = clubTag;
        return this;
    }

    public PlayerFixture updatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public PlayerFixture status(PlayerStatus status) {
        this.status = status;
        return this;
    }

    public Player build() {
        return new Player(tag, name, nameColor, iconId, trophies, highestTrophies, soloRankTier, clubTag, updatedAt, status);
    }
} 
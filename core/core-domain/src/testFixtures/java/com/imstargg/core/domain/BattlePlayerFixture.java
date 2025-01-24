package com.imstargg.core.domain;

import com.imstargg.core.enums.SoloRankTier;
import com.imstargg.test.java.IntegerIncrementUtil;
import com.imstargg.test.java.LongIncrementUtil;

import javax.annotation.Nullable;

public class BattlePlayerFixture {

    private BrawlStarsTag tag = new BrawlStarsTag(String.valueOf(LongIncrementUtil.next()));
    private String name = "Player-" + LongIncrementUtil.next();
    
    @Nullable
    private SoloRankTier soloRankTier = null;
    
    private BattlePlayerBrawler brawler = new BattlePlayerBrawlerFixture().build();

    public BattlePlayerFixture tag(BrawlStarsTag tag) {
        this.tag = tag;
        return this;
    }

    public BattlePlayerFixture name(String name) {
        this.name = name;
        return this;
    }

    public BattlePlayerFixture soloRankTier(@Nullable SoloRankTier soloRankTier) {
        this.soloRankTier = soloRankTier;
        return this;
    }

    public BattlePlayerFixture soloRank() {
        this.soloRankTier = SoloRankTier.values()[IntegerIncrementUtil.next(SoloRankTier.values().length)];
        return this;
    }

    public BattlePlayerFixture brawler(BattlePlayerBrawler brawler) {
        this.brawler = brawler;
        return this;
    }

    public BattlePlayer build() {
        return new BattlePlayer(tag, name, soloRankTier, brawler);
    }
} 
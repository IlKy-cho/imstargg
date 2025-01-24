package com.imstargg.core.domain;

import com.imstargg.core.enums.BattleMode;
import com.imstargg.core.enums.BattleResult;
import com.imstargg.core.enums.BattleType;
import com.imstargg.test.java.IntegerIncrementUtil;
import com.imstargg.test.java.LongIncrementUtil;

import javax.annotation.Nullable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerBattleFixture {

    private OffsetDateTime battleTime = OffsetDateTime.now();
    private PlayerBattleEvent event = new PlayerBattleEventFixture().build();
    private BattleMode mode = BattleMode.values()[IntegerIncrementUtil.next(BattleMode.values().length)];
    private BattleType type = BattleType.values()[IntegerIncrementUtil.next(BattleType.values().length)];
    @Nullable
    private BattleResult result = BattleResult.values()[IntegerIncrementUtil.next(BattleResult.values().length)];
    @Nullable
    private Integer duration = IntegerIncrementUtil.next(10, 300);
    @Nullable
    private Integer rank = IntegerIncrementUtil.next(1, 10);
    @Nullable
    private Integer trophyChange = ThreadLocalRandom.current().nextInt(-13, 13);
    @Nullable
    private BrawlStarsTag starPlayerTag = new BrawlStarsTag("#tag" + LongIncrementUtil.next());
    private List<List<BattlePlayer>> teams = List.of(
            List.of(new BattlePlayerFixture().build(), new BattlePlayerFixture().build(), new BattlePlayerFixture().build()),
            List.of(new BattlePlayerFixture().build(), new BattlePlayerFixture().build(), new BattlePlayerFixture().build())
    );

    public PlayerBattleFixture battleTime(OffsetDateTime battleTime) {
        this.battleTime = battleTime;
        return this;
    }

    public PlayerBattleFixture event(PlayerBattleEvent event) {
        this.event = event;
        return this;
    }

    public PlayerBattleFixture mode(BattleMode mode) {
        this.mode = mode;
        return this;
    }

    public PlayerBattleFixture type(BattleType type) {
        this.type = type;
        return this;
    }

    public PlayerBattleFixture result(@Nullable BattleResult result) {
        this.result = result;
        return this;
    }

    public PlayerBattleFixture duration(@Nullable Integer duration) {
        this.duration = duration;
        return this;
    }

    public PlayerBattleFixture rank(@Nullable Integer rank) {
        this.rank = rank;
        return this;
    }

    public PlayerBattleFixture trophyChange(@Nullable Integer trophyChange) {
        this.trophyChange = trophyChange;
        return this;
    }

    public PlayerBattleFixture starPlayerTag(@Nullable BrawlStarsTag starPlayerTag) {
        this.starPlayerTag = starPlayerTag;
        return this;
    }

    public PlayerBattleFixture teams(List<List<BattlePlayer>> teams) {
        this.teams = teams;
        return this;
    }

    public PlayerBattle build() {
        return new PlayerBattle(battleTime, event, mode, type, result, duration, rank, trophyChange, starPlayerTag, teams);
    }
} 
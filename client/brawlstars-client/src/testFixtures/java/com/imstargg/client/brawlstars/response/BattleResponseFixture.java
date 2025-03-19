package com.imstargg.client.brawlstars.response;

import com.imstargg.core.enums.BattleEventMode;
import com.imstargg.core.enums.BattleMode;
import com.imstargg.core.enums.BattleResult;
import com.imstargg.core.enums.BattleType;
import com.imstargg.test.java.IntegerIncrementUtil;

import java.time.OffsetDateTime;
import java.util.List;

public class BattleResponseFixture {

    private OffsetDateTime battleTime = OffsetDateTime.now();
    private EventResponse event = new EventResponseFixture().build();
    private BattleMode mode = IntegerIncrementUtil.next(BattleMode.values());
    private BattleType type = IntegerIncrementUtil.next(BattleType.values());
    private BattleResult result;
    private Integer duration;
    private Integer rank;
    private Integer trophyChange;
    private BattleResultPlayerResponse starPlayer;
    private List<List<BattleResultPlayerResponse>> teams;
    private List<BattleResultPlayerResponse> players;

    public BattleResponseFixture battleTime(OffsetDateTime battleTime) {
        this.battleTime = battleTime;
        return this;
    }

    public BattleResponseFixture event(EventResponse event) {
        this.event = event;
        return this;
    }

    public BattleResponseFixture mode(BattleMode mode) {
        this.mode = mode;
        return this;
    }

    public BattleResponseFixture type(BattleType type) {
        this.type = type;
        return this;
    }

    public BattleResponseFixture result(BattleResult result) {
        this.result = result;
        return this;
    }

    public BattleResponseFixture duration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public BattleResponseFixture rank(Integer rank) {
        this.rank = rank;
        return this;
    }
    
    public BattleResponseFixture trophyChange(Integer trophyChange) {
        this.trophyChange = trophyChange;
        return this;
    }

    public BattleResponseFixture starPlayer(BattleResultPlayerResponse starPlayer) {
        this.starPlayer = starPlayer;
        return this;
    }

    public BattleResponseFixture teams(List<List<BattleResultPlayerResponse>> teams) {
        this.teams = teams;
        return this;
    }

    public BattleResponseFixture players(List<BattleResultPlayerResponse> players) {
        this.players = players;
        return this;
    }

    public BattleResponse build() {
        return new BattleResponse(
                battleTime,
                event,
                new BattleResultResponse(
                        mode.getCode(),
                        type.getCode(),
                        resultOrDefault(),
                        durationOrDefault(),
                        rankOrDefault(),
                        trophyChangeOrDefault(),
                        starPlayer,
                        teams,
                        players
                )
        );
    }

    private String resultOrDefault() {
        if (result != null) {
            return result.getCode();
        }

        BattleEventMode eventMode = BattleEventMode.find(event.mode());
        return BattleEventMode.showdowns().contains(eventMode)
                ? null : IntegerIncrementUtil.next(BattleResult.values()).getCode();
    }

    private Integer durationOrDefault() {
        if (duration != null) {
            return duration;
        }

        BattleEventMode eventMode = BattleEventMode.find(event.mode());
        return BattleEventMode.showdowns().contains(eventMode)
                ? null : IntegerIncrementUtil.next(10, 300);
    }

    private Integer rankOrDefault() {
        if (rank != null) {
            return rank;
        }

        BattleEventMode eventMode = BattleEventMode.find(event.mode());
        if (BattleEventMode.SOLO_SHOWDOWN == eventMode) {
            return IntegerIncrementUtil.next(1, 10);
        } else if (BattleEventMode.DUO_SHOWDOWN == eventMode) {
            return IntegerIncrementUtil.next(1, 5);
        } else if (BattleEventMode.TRIO_SHOWDOWN == eventMode) {
            return IntegerIncrementUtil.next(1, 4);
        }

        return null;
    }
    
    private Integer trophyChangeOrDefault() {
        if (trophyChange != null) {
            return trophyChange;
        }

        return BattleType.RANKED.equals(type) ? IntegerIncrementUtil.next(-10, 10) : null;
    }
}

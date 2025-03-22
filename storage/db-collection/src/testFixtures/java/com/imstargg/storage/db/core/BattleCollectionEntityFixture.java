package com.imstargg.storage.db.core;

import com.imstargg.core.enums.BattleEventMode;
import com.imstargg.core.enums.BattleMode;
import com.imstargg.core.enums.BattleResult;
import com.imstargg.core.enums.BattleType;
import com.imstargg.storage.db.core.player.BattleCollectionEntity;
import com.imstargg.storage.db.core.player.BattleCollectionEntityEvent;
import com.imstargg.storage.db.core.player.BattleCollectionEntityPlayer;
import com.imstargg.storage.db.core.player.BattleCollectionEntityTeamPlayer;
import com.imstargg.storage.db.core.player.PlayerCollectionEntity;
import com.imstargg.test.java.IntegerIncrementUtil;
import com.imstargg.test.java.LongIncrementUtil;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class BattleCollectionEntityFixture {

    private final String battleKey = UUID.randomUUID().toString();
    private OffsetDateTime battleTime = OffsetDateTime.now();
    private BattleCollectionEntityEvent event = new BattleCollectionEntityEvent(
            LongIncrementUtil.next(),
            BattleEventMode.values()[IntegerIncrementUtil.next(BattleEventMode.values().length)].getCode(),
            "map-" + LongIncrementUtil.next()
    );
    private String mode = BattleEventMode.values()[IntegerIncrementUtil.next(BattleEventMode.values().length)].getCode();
    private String type = BattleType.values()[IntegerIncrementUtil.next(BattleType.values().length)].getCode();
    private String result = BattleResult.values()[IntegerIncrementUtil.next(BattleResult.values().length)].getCode();
    private Integer rank = IntegerIncrementUtil.next(10 + 1);
    private Integer trophies = IntegerIncrementUtil.next(10000);
    private Integer trophyChange = IntegerIncrementUtil.next(14);
    private Integer duration = IntegerIncrementUtil.next();
    private String starPlayerBrawlStarsTag = "#TAG" + LongIncrementUtil.next();
    private PlayerCollectionEntity player = new PlayerCollectionEntityFixture().build();
    private List<List<BattleCollectionEntityTeamPlayer>> teams;

    public BattleCollectionEntityFixture battleTime(OffsetDateTime battleTime) {
        this.battleTime = battleTime;
        return this;
    }

    public BattleCollectionEntityFixture event(BattleCollectionEntityEvent event) {
        this.event = event;
        return this;
    }

    public BattleCollectionEntityFixture mode(BattleMode mode) {
        this.mode = mode.getCode();
        return this;
    }

    public BattleCollectionEntityFixture type(BattleType type) {
        this.type = type.getCode();
        return this;
    }

    public BattleCollectionEntityFixture result(BattleResult result) {
        this.result = result.getCode();
        return this;
    }

    public BattleCollectionEntityFixture rank(Integer rank) {
        this.rank = rank;
        return this;
    }

    public BattleCollectionEntityFixture trophies(Integer trophies) {
        this.trophies = trophies;
        return this;
    }

    public BattleCollectionEntityFixture trophyChange(Integer trophyChange) {
        this.trophyChange = trophyChange;
        return this;
    }

    public BattleCollectionEntityFixture duration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public BattleCollectionEntityFixture starPlayerBrawlStarsTag(String starPlayerBrawlStarsTag) {
        this.starPlayerBrawlStarsTag = starPlayerBrawlStarsTag;
        return this;
    }

    public BattleCollectionEntityFixture player(PlayerCollectionEntity player) {
        this.player = player;
        return this;
    }

    public BattleCollectionEntityFixture teams(List<List<BattleCollectionEntityTeamPlayer>> teams) {
        this.teams = teams;
        return this;
    }

    public BattleCollectionEntity build() {
        return new BattleCollectionEntity(
                battleKey,
                battleTime,
                event,
                mode,
                type,
                result,
                duration,
                starPlayerBrawlStarsTag,
                new BattleCollectionEntityPlayer(
                        player,
                        rank,
                        trophyChange
                ),
                teams != null ? teams : defaultTeams()
        );
    }

    private List<List<BattleCollectionEntityTeamPlayer>> defaultTeams() {
        return List.of(
                List.of(
                        new BattleCollectionEntityTeamPlayerFixture()
                                .brawlStarsTag(player.getBrawlStarsTag())
                                .name(player.getName())
                                .brawler(new BattleCollectionEntityTeamPlayerBrawlerFixture()
                                        .trophies(trophies)
                                        .trophyChange(trophyChange)
                                        .build()
                                )
                                .build(),
                        new BattleCollectionEntityTeamPlayerFixture().build(),
                        new BattleCollectionEntityTeamPlayerFixture().build()
                ),
                List.of(
                        new BattleCollectionEntityTeamPlayerFixture().build(),
                        new BattleCollectionEntityTeamPlayerFixture().build(),
                        new BattleCollectionEntityTeamPlayerFixture().build()
                )
        );
    }
}

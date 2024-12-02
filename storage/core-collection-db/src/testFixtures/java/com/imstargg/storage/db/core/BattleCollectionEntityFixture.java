package com.imstargg.storage.db.core;

import com.imstargg.core.enums.BattleEvent;
import com.imstargg.test.java.LongIncrementUtil;
import jakarta.annotation.Nullable;

import java.time.LocalDateTime;

public class BattleCollectionEntityFixture {

    @Nullable
    private String battleKey;

    @Nullable
    private LocalDateTime battleTime;

    @Nullable
    private Long eventId;

    @Nullable
    private String mode;

    @Nullable
    private String type;

    @Nullable
    private String result;

    @Nullable
    private Integer rank;

    @Nullable
    private Integer duration;

    @Nullable
    private String starPlayerBrawlStarsTag;

    @Nullable
    private PlayerCollectionEntity player;

    @Nullable
    private Long brawlerId;

    @Nullable
    private Integer power;

    @Nullable
    private Integer brawlerTrophies;

    @Nullable
    private Integer trophyChange;

    @Nullable
    private Integer trophySnapshot;

    @Nullable
    private Integer brawlerTrophySnapshot;

    public BattleCollectionEntityFixture battleKey(String battleKey) {
        this.battleKey = battleKey;
        return this;
    }

    public BattleCollectionEntityFixture battleTime(LocalDateTime battleTime) {
        this.battleTime = battleTime;
        return this;
    }

    public BattleCollectionEntityFixture eventId(Long eventId) {
        this.eventId = eventId;
        return this;
    }

    public BattleCollectionEntityFixture mode(String mode) {
        this.mode = mode;
        return this;
    }

    public BattleCollectionEntityFixture type(String type) {
        this.type = type;
        return this;
    }

    public BattleCollectionEntityFixture rank(Integer rank) {
        this.rank = rank;
        return this;
    }

    public BattleCollectionEntityFixture result(String result) {
        this.result = result;
        return this;
    }

    public BattleCollectionEntityFixture duration(int duration) {
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

    public BattleCollectionEntityFixture brawlerId(Long brawlerId) {
        this.brawlerId = brawlerId;
        return this;
    }

    public BattleCollectionEntityFixture power(Integer power) {
        this.power = power;
        return this;
    }

    public BattleCollectionEntityFixture brawlerTrophies(Integer brawlerTrophies) {
        this.brawlerTrophies = brawlerTrophies;
        return this;
    }

    public BattleCollectionEntityFixture trophyChange(Integer trophyChange) {
        this.trophyChange = trophyChange;
        return this;
    }

    public BattleCollectionEntityFixture trophySnapshot(Integer trophySnapshot) {
        this.trophySnapshot = trophySnapshot;
        return this;
    }

    public BattleCollectionEntityFixture brawlerTrophySnapshot(Integer brawlerTrophySnapshot) {
        this.brawlerTrophySnapshot = brawlerTrophySnapshot;
        return this;
    }

    public BattleCollectionEntity build() {
        fillRequiredFields();

        return new BattleCollectionEntity(
                battleKey,
                battleTime,
                new BattleCollectionEntityEvent(
                        BattleEvent.KNOCKOUT_FLARING_PHOENIX.getBrawlStarsId(),
                        BattleEvent.KNOCKOUT_FLARING_PHOENIX.getMode().getName(),
                        BattleEvent.KNOCKOUT_FLARING_PHOENIX.getMap().getName()
                ),
                mode,
                type,
                result,
                duration,
                starPlayerBrawlStarsTag,
                new BattleCollectionEntityPlayer(
                        player,
                        rank,
                        trophyChange,
                        trophySnapshot
                )
        );
    }

    private void fillRequiredFields() {
        long fillKey = LongIncrementUtil.next();

        if (battleKey == null) {
            battleKey = "battleKey-" + fillKey;
        }
        if (battleTime == null) {
            battleTime = LocalDateTime.now();
        }
        if (eventId == null) {
            eventId = LongIncrementUtil.next();
        }
        if (mode == null) {
            mode = "mode-" + fillKey;
        }
        if (type == null) {
            type = "type-" + fillKey;
        }
        if (result == null) {
            result = "result-" + fillKey;
        }
        if (duration == null) {
            duration = 120;
        }
        if (starPlayerBrawlStarsTag == null) {
            starPlayerBrawlStarsTag = "starPlayerBrawlStarsTag-" + fillKey;
        }
        if (player == null) {
            player = new PlayerCollectionEntityFixture().build();
        }
        if (brawlerId == null) {
            brawlerId = fillKey;
        }
        if (power == null) {
            power = 10;
        }
        if (brawlerTrophies == null) {
            brawlerTrophies = 100;
        }
        if (trophyChange == null) {
            trophyChange = 10;
        }
        if (trophySnapshot == null) {
            trophySnapshot = 100;
        }
        if (brawlerTrophySnapshot == null) {
            brawlerTrophySnapshot = 100;
        }
    }
}

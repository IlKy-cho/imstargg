package com.imstargg.storage.db.core;

import com.imstargg.core.enums.BattleEventMode;
import com.imstargg.core.enums.BattleResult;
import com.imstargg.core.enums.BattleType;
import com.imstargg.test.java.IntegerIncrementUtil;
import com.imstargg.test.java.LongIncrementUtil;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class BattleCollectionEntityFixture {

    private final String battleKey = UUID.randomUUID().toString();
    private final OffsetDateTime battleTime = OffsetDateTime.now();
    private final BattleCollectionEntityEvent event = new BattleCollectionEntityEvent(
            LongIncrementUtil.next(),
            BattleEventMode.values()[IntegerIncrementUtil.next(BattleEventMode.values().length)].toString(),
            "map-" + LongIncrementUtil.next()
    );
    private final String mode = BattleEventMode.values()[IntegerIncrementUtil.next(BattleEventMode.values().length)].toString();
    private final String type = BattleType.values()[IntegerIncrementUtil.next(BattleType.values().length)].toString();
    private final String result = BattleResult.values()[IntegerIncrementUtil.next(BattleResult.values().length)].toString();
    private Integer rank;
    private final Integer duration = IntegerIncrementUtil.next();
    private final String starPlayerBrawlStarsTag = "#TAG" + LongIncrementUtil.next();
    private final PlayerCollectionEntity player = new PlayerCollectionEntityFixture().build();
    private final Integer trophyChange = IntegerIncrementUtil.next(14);
    private final List<List<BattleCollectionEntityTeamPlayer>> teams = List.of(
        List.of(
            new BattleCollectionEntityTeamPlayerFixture()
                .brawlStarsTag(player.getBrawlStarsTag())
                .name(player.getName())
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
               teams
        );
    }

}

package com.imstargg.collection.domain;

import com.imstargg.client.brawlstars.response.BattleResponse;
import com.imstargg.client.brawlstars.response.ListResponse;
import com.imstargg.storage.db.core.player.BattleCollectionEntity;
import com.imstargg.storage.db.core.player.PlayerCollectionEntity;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.Comparator;
import java.util.List;

@Component
public class PlayerBattleUpdateApplier {

    private final Clock clock;
    private final BattleCollectionEntityFactory battleEntityFactory;

    public PlayerBattleUpdateApplier(
            Clock clock,
            BattleCollectionEntityFactory battleEntityFactory
    ) {
        this.clock = clock;
        this.battleEntityFactory = battleEntityFactory;
    }

    public List<BattleCollectionEntity> update(
            PlayerCollectionEntity playerEntity,
            ListResponse<BattleResponse> battleListResponse
    ) {
        List<BattleCollectionEntity> battleEntities = battleListResponse.items().stream()
                .map(battleResponse -> battleEntityFactory.create(playerEntity, battleResponse))
                .sorted(Comparator.comparing(BattleCollectionEntity::getBattleTime))
                .toList();
        return playerEntity.battleUpdated(clock, battleEntities);
    }
}

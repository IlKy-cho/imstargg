package com.imstargg.collection.domain;

import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.client.brawlstars.response.BattleResponse;
import com.imstargg.client.brawlstars.response.ListResponse;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class PlayerBattleUpdateProcessor {

    private final BrawlStarsClient brawlStarsClient;
    private final BattleCollectionEntityFactory battleEntityFactory;

    public PlayerBattleUpdateProcessor(
            BrawlStarsClient brawlStarsClient,
            BattleCollectionEntityFactory battleEntityFactory
    ) {
        this.brawlStarsClient = brawlStarsClient;
        this.battleEntityFactory = battleEntityFactory;
    }

    public List<BattleCollectionEntity> update(PlayerCollectionEntity playerEntity) {
        return getBattleResponseListToUpdate(playerEntity).stream()
                .map(battleResponse -> battleEntityFactory.create(playerEntity, battleResponse))
                .sorted(Comparator.comparing(BattleCollectionEntity::getBattleTime))
                .toList();
    }

    private List<BattleResponse> getBattleResponseListToUpdate(PlayerCollectionEntity playerEntity) {
        ListResponse<BattleResponse> battleListResponse = this.brawlStarsClient.getPlayerRecentBattles(
                playerEntity.getBrawlStarsTag());
        return Optional.ofNullable(playerEntity.getLatestBattleTime())
                .map(battle -> battleListResponse.items().stream()
                        .filter(battleResponse -> battleResponse.battleTime().isAfter(
                                playerEntity.getLatestBattleTime()))
                        .toList()
                ).orElse(battleListResponse.items());
    }
}

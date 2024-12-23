package com.imstargg.collection.domain;

import com.imstargg.client.brawlstars.response.BattleResponse;
import com.imstargg.client.brawlstars.response.ListResponse;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class BattleUpdateApplier {

    public List<BattleCollectionEntity> update(
            PlayerCollectionEntity playerEntity,
            ListResponse<BattleResponse> battleListResponse
    ) {
        var updatedBattleList = update(
                playerEntity, battleListResponse, playerEntity.getLatestBattleTime());
        playerEntity.battleUpdated(updatedBattleList.stream().map(BattleCollectionEntity::getBattleTime).toList());

        return updatedBattleList;
    }

    public List<BattleCollectionEntity> update(
            PlayerCollectionEntity playerEntity,
            ListResponse<BattleResponse> battleListResponse,
            @Nullable LocalDateTime latestBattleTime
    ) {
        List<BattleResponse> battleResponseListToUpdate = getBattleResponseListToUpdate(
                battleListResponse, latestBattleTime);

        return battleResponseListToUpdate.stream()
                .map(battleResponse ->
                        new BattleCollectionEntityBuilder(playerEntity, battleResponse).build())
                .sorted(Comparator.comparing(BattleCollectionEntity::getBattleTime))
                .toList();
    }

    private List<BattleResponse> getBattleResponseListToUpdate(
            ListResponse<BattleResponse> battleListResponse, @Nullable LocalDateTime latestBattleTime) {
        return Optional.ofNullable(latestBattleTime)
                .map(battle -> battleListResponse.items().stream()
                        .filter(battleResponse -> battleResponse.battleTime().isAfter(latestBattleTime))
                        .toList())
                .orElse(battleListResponse.items());
    }
}

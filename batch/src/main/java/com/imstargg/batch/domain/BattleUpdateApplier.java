package com.imstargg.batch.domain;

import com.imstargg.client.brawlstars.response.BattleResponse;
import com.imstargg.client.brawlstars.response.ListResponse;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class BattleUpdateApplier {

    public List<BattleCollectionEntity> update(
            PlayerCollectionEntity playerEntity,
            ListResponse<BattleResponse> battleListResponse
    ) {
        return update(playerEntity, battleListResponse, null);
    }

    public List<BattleCollectionEntity> update(
            PlayerCollectionEntity playerEntity,
            ListResponse<BattleResponse> battleListResponse,
            @Nullable BattleCollectionEntity lastBattleEntity
    ) {
        List<BattleResponse> battleResponseListToUpdate = getBattleResponseListToUpdate(
                battleListResponse, lastBattleEntity);

        List<BattleCollectionEntity> battleEntities = new ArrayList<>();
        if (lastBattleEntity != null) {
            lastBattleEntity.notLatest();
            battleEntities.add(lastBattleEntity);
        }

        battleResponseListToUpdate.forEach(battleResponse ->
                battleEntities.add(new BattleCollectionEntityBuilder(playerEntity, battleResponse).build())
        );

        battleEntities.sort(Comparator.comparing(BattleCollectionEntity::getBattleTime));

        if (!battleEntities.isEmpty()) {
            battleEntities.getLast().latest();
        }
        return battleEntities;
    }

    private List<BattleResponse> getBattleResponseListToUpdate(
            ListResponse<BattleResponse> battleListResponse, @Nullable BattleCollectionEntity lastBattleEntity) {
        return Optional.ofNullable(lastBattleEntity)
                .map(battle -> battleListResponse.items().stream()
                        .filter(battleResponse -> battleResponse.battleTime().isAfter(battle.getBattleTime()))
                        .toList())
                .orElse(battleListResponse.items());
    }
}

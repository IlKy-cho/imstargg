package com.imstargg.batch.domain;

import com.imstargg.client.brawlstars.response.BattleResponse;
import com.imstargg.client.brawlstars.response.ListResponse;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;

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

        return battleResponseListToUpdate.stream()
                .map(battleResponse ->
                        new BattleCollectionEntityBuilder(playerEntity, battleResponse).build()
                ).toList();
    }

    private List<BattleResponse> getBattleResponseListToUpdate(
            ListResponse<BattleResponse> battleListResponse, @Nullable BattleCollectionEntity lastBattleEntity) {
        return Optional.ofNullable(lastBattleEntity)
                .map(battle -> battleListResponse.items().stream()
                        .filter(battleResponse -> battleResponse.battleTime().isAfter(battle.getBattleTime()))
                        .toList())
                .orElse(battleListResponse.items())
                .stream()
                .sorted(Comparator.comparing(BattleResponse::battleTime))
                .toList();
    }
}

package com.imstargg.batch.domain;

import com.imstargg.storage.db.core.PlayerBrawlerCollectionEntity;
import com.imstargg.storage.db.core.PlayerCollectionEntity;

import java.util.ArrayList;
import java.util.List;

public record PlayerUpdatedEntity(
        PlayerCollectionEntity playerEntity,
        List<PlayerBrawlerCollectionEntity> playerBrawlerEntities,
        List<BattleUpdateResult> battleUpdateResults
) {

    public List<Object> toList() {
        List<Object> results = new ArrayList<>();
        results.add(playerEntity);
        results.addAll(playerBrawlerEntities);
        results.addAll(battleUpdateResults.stream()
                .map(BattleUpdateResult::battleEntity).toList()
        );
        results.addAll(
                battleUpdateResults.stream()
                        .flatMap(battleResult -> battleResult.battlePlayerEntities().stream())
                        .toList()
        );
        return results;
    }
}

package com.imstargg.collection.domain;

import com.imstargg.client.brawlstars.response.BattleResponse;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import org.springframework.stereotype.Component;

@Component
public class BattleCollectionEntityFactory {

    public BattleCollectionEntity create(PlayerCollectionEntity playerEntity, BattleResponse battleResponse) {
        return new BattleCollectionEntityBuilder(playerEntity, battleResponse).build();
    }
}

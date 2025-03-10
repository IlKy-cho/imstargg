package com.imstargg.collection.domain;

import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import org.springframework.stereotype.Component;

import java.time.Clock;

@Component
public class PlayerUpdaterFactory {

    private final Clock clock;
    private final PlayerUpdateProcessor playerUpdateProcessor;
    private final BrawlStarsClient brawlStarsClient;
    private final BattleCollectionEntityFactory battleEntityFactory;

    public PlayerUpdaterFactory(
            Clock clock,
            BrawlStarsClient brawlStarsClient,
            PlayerUpdateProcessor playerUpdateProcessor,
            BattleCollectionEntityFactory battleEntityFactory
    ) {
        this.clock = clock;
        this.brawlStarsClient = brawlStarsClient;
        this.playerUpdateProcessor = playerUpdateProcessor;
        this.battleEntityFactory = battleEntityFactory;
    }

    public PlayerUpdater create(PlayerCollectionEntity playerEntity) {
        return new PlayerUpdater(
                clock,
                playerUpdateProcessor,
                brawlStarsClient,
                battleEntityFactory,
                playerEntity
        );
    }
}

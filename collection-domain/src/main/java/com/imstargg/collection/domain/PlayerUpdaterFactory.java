package com.imstargg.collection.domain;

import com.imstargg.storage.db.core.PlayerCollectionEntity;
import org.springframework.stereotype.Component;

import java.time.Clock;

@Component
public class PlayerUpdaterFactory {

    private final Clock clock;
    private final PlayerUpdateProcessor playerUpdateProcessor;
    private final PlayerBattleUpdateProcessor playerBattleUpdateProcessor;

    public PlayerUpdaterFactory(
            Clock clock,
            PlayerUpdateProcessor playerUpdateProcessor,
            PlayerBattleUpdateProcessor playerBattleUpdateProcessor
    ) {
        this.clock = clock;
        this.playerUpdateProcessor = playerUpdateProcessor;
        this.playerBattleUpdateProcessor = playerBattleUpdateProcessor;
    }

    public PlayerUpdater create(PlayerCollectionEntity playerEntity) {
        return new PlayerUpdater(
                clock,
                playerUpdateProcessor,
                playerBattleUpdateProcessor,
                playerEntity
        );
    }
}

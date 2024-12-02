package com.imstargg.batch.domain;

import com.imstargg.storage.db.core.PlayerCollectionEntity;
import com.imstargg.storage.db.core.PlayerCollectionJpaRepository;
import com.imstargg.storage.db.core.UnknownPlayerCollectionEntity;
import com.imstargg.storage.db.core.UnknownPlayerCollectionJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class PlayerDeleter {

    private final PlayerCollectionJpaRepository playerRepository;
    private final UnknownPlayerCollectionJpaRepository unknownPlayerRepository;

    public PlayerDeleter(
            PlayerCollectionJpaRepository playerRepository,
            UnknownPlayerCollectionJpaRepository unknownPlayerRepository
    ) {
        this.playerRepository = playerRepository;
        this.unknownPlayerRepository = unknownPlayerRepository;
    }

    public void delete(PlayerCollectionEntity player) {
        player.delete();
        playerRepository.save(player);
    }

    public void delete(UnknownPlayerCollectionEntity player) {
        player.delete();
        unknownPlayerRepository.save(player);
    }
}

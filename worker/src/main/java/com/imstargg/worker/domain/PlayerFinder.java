package com.imstargg.worker.domain;

import com.imstargg.storage.db.core.PlayerCollectionEntity;
import com.imstargg.storage.db.core.PlayerCollectionJpaRepository;
import com.imstargg.storage.db.core.UnknownPlayerCollectionEntity;
import com.imstargg.storage.db.core.UnknownPlayerCollectionJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PlayerFinder {

    private final PlayerCollectionJpaRepository playerJpaRepository;
    private final UnknownPlayerCollectionJpaRepository unknownPlayerJpaRepository;

    public PlayerFinder(
            PlayerCollectionJpaRepository playerJpaRepository,
            UnknownPlayerCollectionJpaRepository unknownPlayerJpaRepository
    ) {
        this.playerJpaRepository = playerJpaRepository;
        this.unknownPlayerJpaRepository = unknownPlayerJpaRepository;
    }

    public Optional<PlayerCollectionEntity> findPlayer(String brawlStarsTag) {
        return playerJpaRepository.findVersionedWithBrawlersByBrawlStarsTag(brawlStarsTag);
    }

    public Optional<UnknownPlayerCollectionEntity> findUnknownPlayer(String brawlStarsTag) {
        return unknownPlayerJpaRepository.findVersionedByBrawlStarsTag(brawlStarsTag);
    }
}

package com.imstargg.worker.domain;

import com.imstargg.storage.db.core.PlayerRenewalCollectionEntity;
import com.imstargg.storage.db.core.PlayerRenewalCollectionJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class PlayerRenewalUpdater {

    private final PlayerRenewalCollectionJpaRepository playerRenewalCollectionJpaRepository;

    public PlayerRenewalUpdater(PlayerRenewalCollectionJpaRepository playerRenewalCollectionJpaRepository) {
        this.playerRenewalCollectionJpaRepository = playerRenewalCollectionJpaRepository;
    }

    public void update(PlayerRenewalCollectionEntity playerRenewalEntity) {
        playerRenewalCollectionJpaRepository.save(playerRenewalEntity);
    }
}

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

    public void executing(PlayerRenewalCollectionEntity playerRenewalEntity) {
        playerRenewalEntity.executing();
        playerRenewalCollectionJpaRepository.save(playerRenewalEntity);
    }

    public void complete(PlayerRenewalCollectionEntity playerRenewalEntity) {
        playerRenewalEntity.complete();
        playerRenewalCollectionJpaRepository.save(playerRenewalEntity);
    }

    public void inMaintenance(PlayerRenewalCollectionEntity playerRenewalEntity) {
        playerRenewalEntity.inMaintenance();
        playerRenewalCollectionJpaRepository.save(playerRenewalEntity);
    }

    public void failed(PlayerRenewalCollectionEntity playerRenewalEntity) {
        playerRenewalEntity.failed();
        playerRenewalCollectionJpaRepository.save(playerRenewalEntity);
    }
}

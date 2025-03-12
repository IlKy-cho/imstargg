package com.imstargg.worker.domain;

import com.imstargg.storage.db.core.PlayerRenewalCollectionEntity;
import com.imstargg.storage.db.core.PlayerRenewalCollectionJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PlayerRenewalRepository {

    private final PlayerRenewalCollectionJpaRepository playerRenewalCollectionJpaRepository;

    public PlayerRenewalRepository(PlayerRenewalCollectionJpaRepository playerRenewalCollectionJpaRepository) {
        this.playerRenewalCollectionJpaRepository = playerRenewalCollectionJpaRepository;
    }

    @Transactional
    public void executing(PlayerRenewalCollectionEntity playerRenewalEntity) {
        PlayerRenewalCollectionEntity updatedEntity = playerRenewalCollectionJpaRepository.findWithOptimisticLockById(
                        playerRenewalEntity.getId()
                ).orElseThrow(() -> new IllegalStateException("플레이어 갱신 정보가 존재하지 않습니다. " +
                        "id=" + playerRenewalEntity.getId() + ", tag=" + playerRenewalEntity.getBrawlStarsTag()));
        updatedEntity.executing();
    }

    @Transactional
    public void complete(PlayerRenewalCollectionEntity playerRenewalEntity) {
        playerRenewalCollectionJpaRepository.findById(playerRenewalEntity.getId())
                .ifPresent(PlayerRenewalCollectionEntity::complete);
    }

    @Transactional
    public void inMaintenance(PlayerRenewalCollectionEntity playerRenewalEntity) {
        playerRenewalCollectionJpaRepository.findById(playerRenewalEntity.getId())
                .ifPresent(PlayerRenewalCollectionEntity::inMaintenance);
    }

    @Transactional
    public void failed(PlayerRenewalCollectionEntity playerRenewalEntity) {
        playerRenewalCollectionJpaRepository.findById(playerRenewalEntity.getId())
                .ifPresent(PlayerRenewalCollectionEntity::failed);
    }
}

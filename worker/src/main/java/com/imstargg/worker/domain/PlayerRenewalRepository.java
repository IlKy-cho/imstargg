package com.imstargg.worker.domain;

import com.imstargg.storage.db.core.player.PlayerRenewalCollectionEntity;
import com.imstargg.storage.db.core.player.PlayerRenewalCollectionJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.time.Clock;

@Repository
public class PlayerRenewalRepository {

    private final Clock clock;
    private final PlayerRenewalCollectionJpaRepository playerRenewalCollectionJpaRepository;

    public PlayerRenewalRepository(
            Clock clock,
            PlayerRenewalCollectionJpaRepository playerRenewalCollectionJpaRepository
    ) {
        this.clock = clock;
        this.playerRenewalCollectionJpaRepository = playerRenewalCollectionJpaRepository;
    }

    public boolean canRenew(String brawlStarsTag) {
        PlayerRenewalCollectionEntity playerRenewal = playerRenewalCollectionJpaRepository
                .findByBrawlStarsTag(brawlStarsTag)
                .orElseThrow(() -> notFoundException(brawlStarsTag));
        return playerRenewal.getStatus().canRenew();
    }

    @Transactional
    public void executing(String brawlStarsTag) {
        PlayerRenewalCollectionEntity playerRenewal = playerRenewalCollectionJpaRepository
                .findWithOptimisticLockByBrawlStarsTag(brawlStarsTag)
                .orElseThrow(() -> notFoundException(brawlStarsTag));
        playerRenewal.executing();
    }

    @Transactional
    public void complete(String brawlStarsTag) {
        PlayerRenewalCollectionEntity playerRenewal = playerRenewalCollectionJpaRepository
                .findWithOptimisticLockByBrawlStarsTag(brawlStarsTag)
                .orElseThrow(() -> notFoundException(brawlStarsTag));
        playerRenewal.complete();
    }

    @Transactional
    public void inMaintenance(String brawlStarsTag) {
        PlayerRenewalCollectionEntity playerRenewal = playerRenewalCollectionJpaRepository
                .findWithOptimisticLockByBrawlStarsTag(brawlStarsTag)
                .orElseThrow(() -> notFoundException(brawlStarsTag));
        playerRenewal.inMaintenance();
    }

    @Transactional
    public void failed(String brawlStarsTag) {
        PlayerRenewalCollectionEntity playerRenewal = playerRenewalCollectionJpaRepository
                .findWithOptimisticLockByBrawlStarsTag(brawlStarsTag)
                .orElseThrow(() -> notFoundException(brawlStarsTag));
        playerRenewal.failed();
    }

    private static IllegalStateException notFoundException(String brawlStarsTag) {
        return new IllegalStateException("플레이어 갱신 정보가 존재하지 않습니다. tag=" + brawlStarsTag);
    }

}

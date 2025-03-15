package com.imstargg.worker.domain;

import com.imstargg.storage.db.core.PlayerRenewalCollectionEntity;
import com.imstargg.storage.db.core.PlayerRenewalCollectionJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PlayerRenewalReader {

    private final PlayerRenewalCollectionJpaRepository playerRenewalCollectionJpaRepository;

    public PlayerRenewalReader(PlayerRenewalCollectionJpaRepository playerRenewalCollectionJpaRepository) {
        this.playerRenewalCollectionJpaRepository = playerRenewalCollectionJpaRepository;
    }

    @Transactional
    public PlayerRenewalCollectionEntity get(String brawlStarsTag) {
        return playerRenewalCollectionJpaRepository.findVersionedByBrawlStarsTag(brawlStarsTag)
                .orElseThrow(() -> new IllegalStateException("플레이어 갱신 정보가 존재하지 않습니다. " +
                        "tag=" + brawlStarsTag
                ));
    }
}

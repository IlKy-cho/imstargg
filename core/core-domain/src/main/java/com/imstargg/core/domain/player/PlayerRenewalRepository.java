package com.imstargg.core.domain.player;

import com.imstargg.core.domain.BrawlStarsTag;
import com.imstargg.core.enums.PlayerRenewalStatus;
import com.imstargg.core.error.CoreException;
import com.imstargg.storage.db.core.PlayerRenewalEntity;
import com.imstargg.storage.db.core.PlayerRenewalJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PlayerRenewalRepository {

    private static final Logger log = LoggerFactory.getLogger(PlayerRenewalRepository.class);

    private final PlayerRenewalJpaRepository playerRenewalJpaRepository;

    public PlayerRenewalRepository(PlayerRenewalJpaRepository playerRenewalJpaRepository) {
        this.playerRenewalJpaRepository = playerRenewalJpaRepository;
    }


    public PlayerRenewal get(BrawlStarsTag tag) {
        PlayerRenewalEntity entity = playerRenewalJpaRepository.findByBrawlStarsTag(tag.value())
                .orElseGet(() -> playerRenewalJpaRepository.save(
                        new PlayerRenewalEntity(tag.value())
                ));

        return new PlayerRenewal(
                new BrawlStarsTag(entity.getBrawlStarsTag()),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }

    public Optional<PlayerRenewal> find(BrawlStarsTag tag) {
        return playerRenewalJpaRepository.findByBrawlStarsTag(tag.value())
                .map(entity -> new PlayerRenewal(
                        new BrawlStarsTag(entity.getBrawlStarsTag()),
                        entity.getStatus(),
                        entity.getCreatedAt()
                ));
    }


    public boolean pending(PlayerRenewal playerRenewal) {
        PlayerRenewalEntity entity = playerRenewalJpaRepository
                .findWithOptimisticLockByBrawlStarsTag(playerRenewal.tag().value())
                .orElseThrow(() -> new CoreException("플레이어 갱신 요청을 찾을 수 없습니다. playerTag=" + playerRenewal.tag()));

        entity.pending();
        try {
            playerRenewalJpaRepository.save(entity);
            return true;
        } catch (OptimisticLockingFailureException e) {
            log.debug("플레이어 갱신 pending 업데이트 실패. playerTag={}", playerRenewal.tag(), e);
            return false;
        }
    }

    public int countRenewing() {
        return (int) playerRenewalJpaRepository.countByStatusIn(PlayerRenewalStatus.renewingList());
    }
}

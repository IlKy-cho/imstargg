package com.imstargg.core.domain.player;

import com.imstargg.core.domain.BrawlStarsTag;
import com.imstargg.core.enums.PlayerRenewalStatus;
import com.imstargg.core.error.CoreException;
import com.imstargg.storage.db.core.player.PlayerRenewalEntity;
import com.imstargg.storage.db.core.player.PlayerRenewalJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class PlayerRenewalRepository {

    private static final Logger log = LoggerFactory.getLogger(PlayerRenewalRepository.class);

    private final PlayerRenewalJpaRepository playerRenewalJpaRepository;
    private final Inner inner;

    public PlayerRenewalRepository(
            PlayerRenewalJpaRepository playerRenewalJpaRepository,
            Inner inner
    ) {
        this.playerRenewalJpaRepository = playerRenewalJpaRepository;
        this.inner = inner;
    }


    public PlayerRenewal get(BrawlStarsTag tag) {
        return mapEntityToPlayerRenewal(
                playerRenewalJpaRepository.findByBrawlStarsTag(tag.value())
                        .orElseGet(() -> playerRenewalJpaRepository.save(
                                new PlayerRenewalEntity(tag.value())
                        ))
        );
    }

    public Optional<PlayerRenewal> find(BrawlStarsTag tag) {
        return playerRenewalJpaRepository.findByBrawlStarsTag(tag.value())
                .map(this::mapEntityToPlayerRenewal);
    }

    private PlayerRenewal mapEntityToPlayerRenewal(PlayerRenewalEntity entity) {
        return new PlayerRenewal(
                new BrawlStarsTag(entity.getBrawlStarsTag()),
                entity.getStatus(),
                entity.getUpdatedAt()
        );
    }

    public boolean pending(PlayerRenewal playerRenewal) {
        try {
            inner.pending(playerRenewal);
            return true;
        } catch (OptimisticLockingFailureException e) {
            log.debug("플레이어 갱신 pending 업데이트 실패. playerTag={}", playerRenewal.tag(), e);
            return false;
        }
    }

    public int countRenewing() {
        return (int) playerRenewalJpaRepository.countByStatusIn(PlayerRenewalStatus.renewingList());
    }

    @Component
    public static class Inner {

        private final PlayerRenewalJpaRepository playerRenewalJpaRepository;

        public Inner(
                PlayerRenewalJpaRepository playerRenewalJpaRepository
        ) {
            this.playerRenewalJpaRepository = playerRenewalJpaRepository;
        }

        @Transactional
        public void pending(PlayerRenewal playerRenewal) {
            PlayerRenewalEntity entity = playerRenewalJpaRepository
                    .findVersionedByBrawlStarsTag(playerRenewal.tag().value())
                    .orElseThrow(() -> new CoreException(
                            "플레이어 갱신 요청을 찾을 수 없습니다. playerTag=" + playerRenewal.tag()));
            entity.pending();
        }
    }
}

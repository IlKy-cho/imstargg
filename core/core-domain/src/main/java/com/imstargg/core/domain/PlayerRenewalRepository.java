package com.imstargg.core.domain;

import com.imstargg.core.enums.PlayerRenewalStatus;
import com.imstargg.core.error.CoreException;
import com.imstargg.storage.db.core.PlayerRenewalEntity;
import com.imstargg.storage.db.core.PlayerRenewalJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class PlayerRenewalRepository {

    private static final Logger log = LoggerFactory.getLogger(PlayerRenewalRepository.class);

    private final Clock clock;
    private final PlayerRenewalJpaRepository playerRenewalJpaRepository;
    private final Inner inner;

    public PlayerRenewalRepository(
            Clock clock,
            PlayerRenewalJpaRepository playerRenewalJpaRepository,
            Inner inner
    ) {
        this.clock = clock;
        this.playerRenewalJpaRepository = playerRenewalJpaRepository;
        this.inner = inner;
    }


    public PlayerRenewal get(BrawlStarsTag tag) {
        PlayerRenewalEntity entity = playerRenewalJpaRepository.findByBrawlStarsTag(tag.value())
                .orElseGet(() -> playerRenewalJpaRepository.save(
                        new PlayerRenewalEntity(tag.value(), LocalDateTime.now(clock))
                ));

        return new PlayerRenewal(entity.getStatus(), entity.getUpdatedAt());
    }

    public Optional<PlayerRenewal> find(BrawlStarsTag tag) {
        return playerRenewalJpaRepository.findByBrawlStarsTag(tag.value())
                .map(entity -> new PlayerRenewal(entity.getStatus(), entity.getUpdatedAt()));
    }


    public boolean pending(BrawlStarsTag tag) {
        try {
            inner.pending(tag);
            return true;
        } catch (OptimisticLockingFailureException e) {
            log.debug("플레이어 갱신 pending 업데이트 실패. playerTag={}", tag, e);
            return false;
        }
    }

    public int countRenewing() {
        return (int) playerRenewalJpaRepository.countByStatusIn(PlayerRenewalStatus.renewingList());
    }

    @Component
    public static class Inner {

        private final Clock clock;
        private final PlayerRenewalJpaRepository playerRenewalJpaRepository;

        public Inner(
                Clock clock,
                PlayerRenewalJpaRepository playerRenewalJpaRepository
        ) {
            this.clock = clock;
            this.playerRenewalJpaRepository = playerRenewalJpaRepository;
        }

        @Transactional
        public void pending(BrawlStarsTag tag) {
            PlayerRenewalEntity entity = playerRenewalJpaRepository.findWithOptimisticLockByBrawlStarsTag(tag.value())
                    .orElseThrow(() -> new CoreException("플레이어 갱신 요청을 찾을 수 없습니다. playerTag=" + tag));

            entity.pending(LocalDateTime.now(clock));
            playerRenewalJpaRepository.save(entity);
        }
    }
}

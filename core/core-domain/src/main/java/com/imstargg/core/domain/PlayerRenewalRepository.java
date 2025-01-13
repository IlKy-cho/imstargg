package com.imstargg.core.domain;

import com.imstargg.core.enums.PlayerRenewalStatus;
import com.imstargg.core.error.CoreErrorType;
import com.imstargg.core.error.CoreException;
import com.imstargg.storage.db.core.PlayerRenewalEntity;
import com.imstargg.storage.db.core.PlayerRenewalJpaRepository;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PlayerRenewalRepository {

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
        PlayerRenewalEntity entity = playerRenewalJpaRepository.findByBrawlStarsTag(tag.value())
                .orElseGet(() -> playerRenewalJpaRepository.save(new PlayerRenewalEntity(tag.value())));

        return new PlayerRenewal(entity.getStatus(), entity.getUpdatedAt());
    }

    @Retryable(
            retryFor = OptimisticLockingFailureException.class,
            backoff = @Backoff(delay = 200, multiplier = 3, maxDelay = 2000, random = true)
    )
    public void pending(BrawlStarsTag tag) {
        inner.pending(tag);
    }

    @Component
    public static class Inner {

        private final PlayerRenewalJpaRepository playerRenewalJpaRepository;

        public Inner(PlayerRenewalJpaRepository playerRenewalJpaRepository) {
            this.playerRenewalJpaRepository = playerRenewalJpaRepository;
        }

        @Transactional
        public void pending(BrawlStarsTag tag) {
            PlayerRenewalEntity entity = playerRenewalJpaRepository.findWithOptimisticLockByBrawlStarsTag(tag.value())
                    .orElseGet(() -> playerRenewalJpaRepository.save(new PlayerRenewalEntity(tag.value())));

            if (entity.getStatus() == PlayerRenewalStatus.PENDING) {
                throw new CoreException(CoreErrorType.PLAYER_ALREADY_RENEWAL_REQUESTED, "playerTag=" + tag);
            }

            entity.pending();
            playerRenewalJpaRepository.save(entity);
        }
    }
}

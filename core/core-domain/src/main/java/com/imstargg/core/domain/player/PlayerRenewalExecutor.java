package com.imstargg.core.domain.player;

import com.imstargg.core.domain.BrawlStarsTag;
import com.imstargg.core.enums.PlayerRenewalStatus;
import com.imstargg.core.error.CoreErrorType;
import com.imstargg.core.error.CoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.Optional;

@Component
public class PlayerRenewalExecutor {

    private static final Logger log = LoggerFactory.getLogger(PlayerRenewalExecutor.class);

    private final Clock clock;
    private final PlayerRepository playerRepository;
    private final PlayerRenewalRepository playerRenewalRepository;
    private final PlayerRenewEventPublisher eventPublisher;

    public PlayerRenewalExecutor(
            Clock clock,
            PlayerRepository playerRepository,
            PlayerRenewalRepository playerRenewalRepository,
            PlayerRenewEventPublisher eventPublisher
    ) {
        this.clock = clock;
        this.playerRepository = playerRepository;
        this.playerRenewalRepository = playerRenewalRepository;
        this.eventPublisher = eventPublisher;
    }

    public void renewNew(UnknownPlayer unknownPlayer) {
        validateRequestCount();
        PlayerRenewal playerRenewal = playerRenewalRepository.get(unknownPlayer.tag());
        if (!playerRenewal.available(unknownPlayer, clock)) {
            throw new CoreException(CoreErrorType.PLAYER_RENEWAL_UNAVAILABLE,
                    "갱신이 불가능 합니다. PlayerRenewal=" + playerRenewal + "unknownPlayerTag=" + unknownPlayer.tag());
        }

        if (!playerRenewalRepository.pending(playerRenewal)) {
            throw new CoreException(CoreErrorType.PLAYER_RENEWAL_UNAVAILABLE,
                    "갱신 요청 충돌. unknownPlayerTag=" + unknownPlayer.tag());
        }

        eventPublisher.publish(unknownPlayer.tag());
    }

    public void renew(Player player) {
        validateRequestCount();
        PlayerRenewal playerRenewal = playerRenewalRepository.get(player.tag());
        if (!playerRenewal.available(player, clock)) {
            throw new CoreException(CoreErrorType.PLAYER_RENEWAL_UNAVAILABLE,
                    "갱신이 불가능 합니다. PlayerRenewal=" + playerRenewal + " playerTag=" + player.tag());
        }

        if (!playerRenewalRepository.pending(playerRenewal)) {
            throw new CoreException(CoreErrorType.PLAYER_RENEWAL_UNAVAILABLE,
                    "갱신 요청 충돌. playerTag=" + player.tag());
        }

        eventPublisher.publish(player.tag());
    }

    public void validateRequestCount() {
        int renewingCount = playerRenewalRepository.countRenewing();
        log.debug("갱신 처리 중인 플레이어 수: {}", renewingCount);
        if (renewingCount > 1000) {
            throw new CoreException(CoreErrorType.PLAYER_RENEWAL_TOO_MANY, "renewingCount=" + renewingCount);
        }
    }

    public boolean isRenewing(Player player) {
        return doIsRenewing(player.tag());
    }

    public boolean isRenewingNew(BrawlStarsTag tag) {
        UnknownPlayer unknownPlayer = playerRepository.getUnknown(tag);
        return doIsRenewing(unknownPlayer.tag());
    }

    private boolean doIsRenewing(BrawlStarsTag tag) {
        Optional<PlayerRenewal> playerRenewalOpt = playerRenewalRepository.find(tag);
        if (playerRenewalOpt.isEmpty()) {
            return false;
        }
        PlayerRenewal playerRenewal = playerRenewalOpt.get();
        if (playerRenewal.status().renewing()) {
            return true;
        }

        if (PlayerRenewalStatus.IN_MAINTENANCE.equals(playerRenewal.status())) {
            throw new CoreException(CoreErrorType.BRAWLSTARS_IN_MAINTENANCE, "playerTag=" + tag);
        }

        return false;
    }
}

package com.imstargg.core.domain;

import com.imstargg.core.enums.PlayerRenewalStatus;
import com.imstargg.core.error.CoreErrorType;
import com.imstargg.core.error.CoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Clock;

@Component
public class PlayerRenewer {

    private static final Logger log = LoggerFactory.getLogger(PlayerRenewer.class);

    private final Clock clock;
    private final PlayerRepository playerRepository;
    private final PlayerRenewalRepository playerRenewalRepository;
    private final PlayerRenewEventPublisher eventPublisher;

    public PlayerRenewer(
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

    public void renewNew(BrawlStarsTag tag) {
        validateRequestCount();

        UnknownPlayer unknownPlayer = playerRepository.getUnknown(tag);
        if (unknownPlayer.updateAvailable(clock)) {
            throw new CoreException(CoreErrorType.PLAYER_RENEWAL_UNAVAILABLE, "unknownPlayerTag=" + tag);
        }

        playerRepository.updateSearchNew(unknownPlayer);
        eventPublisher.publish(tag);
    }

    public void renew(Player player) {
        validateRequestCount();
        PlayerRenewal playerRenewal = playerRenewalRepository.get(player.tag());
        if (!playerRenewal.available(player, clock)) {
            throw new CoreException(CoreErrorType.PLAYER_RENEWAL_UNAVAILABLE, "playerTag=" + player.tag());
        }

        if (!playerRenewalRepository.pending(player.tag())) {
            throw new CoreException(CoreErrorType.PLAYER_RENEWAL_UNAVAILABLE, "playerTag=" + player.tag());
        }

        eventPublisher.publish(player.tag());
    }

    public void validateRequestCount() {
        int renewRequestedCount = playerRepository.countRenewRequested();
        log.debug("갱신 요청된 플레이어 수: {}", renewRequestedCount);
        if (renewRequestedCount > 1000) {
            throw new CoreException(CoreErrorType.PLAYER_RENEWAL_TOO_MANY);
        }
    }

    public boolean isRenewing(Player player) {
        return playerRenewalRepository.find(player.tag())
                .map(PlayerRenewal::status)
                .map(PlayerRenewalStatus::renewing)
                .orElse(false);
    }

    public boolean isRenewingNew(BrawlStarsTag tag) {
        return playerRepository.findNew(tag)
                .map(UnknownPlayer::isRenewing)
                .orElseThrow(() -> new CoreException(CoreErrorType.PLAYER_NOT_FOUND, "playerTag=" + tag));
    }
}

package com.imstargg.core.domain;

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
    private final PlayerRenewEventPublisher eventPublisher;

    public PlayerRenewer(Clock clock, PlayerRepository playerRepository, PlayerRenewEventPublisher eventPublisher) {
        this.clock = clock;
        this.playerRepository = playerRepository;
        this.eventPublisher = eventPublisher;
    }

    public void renewNew(BrawlStarsTag tag) {
        validateRequestCount();

        UnknownPlayer unknownPlayer = playerRepository.getUnknown(tag);
        if (unknownPlayer.updateAvailable(clock)) {
            throw new CoreException(CoreErrorType.PLAYER_ALREADY_RENEWED, "unknownPlayerTag=" + tag);
        }

        playerRepository.updateSearchNew(unknownPlayer);
        eventPublisher.publish(tag);
    }

    public void renew(Player player) {
        validateRequestCount();
        if (!player.renewAvailable(clock)) {
            throw new CoreException(CoreErrorType.PLAYER_ALREADY_RENEWED, "playerTag=" + player.tag());
        }
        playerRepository.renewRequested(player);
        eventPublisher.publish(player.tag());
    }

    public void validateRequestCount() {
        int renewRequestedCount = playerRepository.countRenewRequested();
        log.debug("갱신 요청된 플레이어 수: {}", renewRequestedCount);
        if (renewRequestedCount > 1000) {
            throw new CoreException(CoreErrorType.PLAYER_RENEW_UNAVAILABLE);
        }
    }

    public boolean isRenewing(BrawlStarsTag tag) {
        return playerRepository.findByTag(tag)
                .map(Player::isRenewing)
                .orElseThrow(() -> new CoreException(CoreErrorType.PLAYER_NOT_FOUND, "playerTag=" + tag));
    }

    public boolean isRenewingNew(BrawlStarsTag tag) {
        return playerRepository.findNew(tag)
                .map(UnknownPlayer::isRenewing)
                .orElseThrow(() -> new CoreException(CoreErrorType.PLAYER_NOT_FOUND, "playerTag=" + tag));
    }
}

package com.imstargg.core.domain;

import com.imstargg.core.enums.UnknownPlayerStatus;
import com.imstargg.core.error.CoreErrorType;
import com.imstargg.core.error.CoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

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

    public boolean renewNew(BrawlStarsTag tag) {
        NewPlayer newPlayer = playerRepository.getNew(tag);
        if (newPlayer.updateAvailableAt().isAfter(LocalDateTime.now(clock))) {
            return false;
        }

        eventPublisher.publishNew(tag);
        return true;
    }

    public void renew(Player player) {
        if (!player.isNextUpdateCooldownOver(clock)) {
            throw new CoreException(CoreErrorType.PLAYER_ALREADY_RENEWED, "playerTag=" + player.tag());
        }
        if (player.status().isRenewing()) {
            log.info("이미 플레이어가 갱신 중입니다. playerTag={}, status={}", player.tag(), player.status());
            return;
        }
        playerRepository.renewRequested(player);
        eventPublisher.publish(player);
    }

    public boolean isRenewing(BrawlStarsTag tag) {
        return playerRepository.findByTag(tag)
                .map(player -> player.status().isRenewing())
                .or(() -> playerRepository.findNew(tag)
                        .map(player -> player.status() == UnknownPlayerStatus.SEARCH_NEW)
                )
                .orElseThrow(() -> new CoreException(CoreErrorType.PLAYER_NOT_FOUND, "playerTag=" + tag));
    }
}

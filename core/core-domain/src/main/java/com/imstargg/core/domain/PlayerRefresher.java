package com.imstargg.core.domain;

import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

@Component
public class PlayerRefresher {

    private final Clock clock;
    private final PlayerRepository playerRepository;
    private final PlayerRefreshEventPublisher eventPublisher;

    public PlayerRefresher(Clock clock, PlayerRepository playerRepository, PlayerRefreshEventPublisher eventPublisher) {
        this.clock = clock;
        this.playerRepository = playerRepository;
        this.eventPublisher = eventPublisher;
    }

    public boolean refreshNew(BrawlStarsTag tag) {
        NewPlayer newPlayer = playerRepository.getNew(tag);
        if (newPlayer.updateAvailableAt().isAfter(LocalDateTime.now(clock))) {
            return false;
        }

        eventPublisher.publishNew(tag);
        return true;
    }
}

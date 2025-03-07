package com.imstargg.core.domain.player;

import com.imstargg.core.domain.BrawlStarsTag;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PlayerFinder {

    private final PlayerRepository playerRepository;

    public PlayerFinder(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Optional<Player> find(BrawlStarsTag tag) {
        return playerRepository.findByTag(tag);
    }
}

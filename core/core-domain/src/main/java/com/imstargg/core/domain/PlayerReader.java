package com.imstargg.core.domain;

import com.imstargg.core.error.CoreErrorType;
import com.imstargg.core.error.CoreException;
import org.springframework.stereotype.Component;

@Component
public class PlayerReader {

    private final PlayerRepository playerRepository;

    public PlayerReader(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player get(BrawlStarsTag tag) {
        return playerRepository.findByTag(tag)
                .orElseThrow(() -> new CoreException(CoreErrorType.PLAYER_NOT_FOUND, "playerTag=" + tag));
    }
}

package com.imstargg.core.domain.player;

import com.imstargg.core.domain.BrawlStarsTag;
import com.imstargg.core.error.CoreErrorType;
import com.imstargg.core.error.CoreException;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public List<PlayerBrawler> getBrawlers(Player player) {
        return playerRepository.findBrawlers(player);
    }

    public UnknownPlayer getUnknown(BrawlStarsTag tag) {
        if (playerRepository.findByTag(tag).isPresent()) {
            throw new CoreException(CoreErrorType.PLAYER_ALREADY_EXISTS, "playerTag=" + tag);
        }
        return playerRepository.getUnknown(tag);
    }
}

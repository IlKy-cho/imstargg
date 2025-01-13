package com.imstargg.core.domain;

import com.imstargg.core.error.CoreErrorType;
import com.imstargg.core.error.CoreException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    private final PlayerReader playerReader;
    private final PlayerFinder playerFinder;
    private final PlayerRenewer playerRenewer;

    public PlayerService(
            PlayerReader playerReader,
            PlayerFinder playerFinder,
            PlayerRenewer playerRenewer
    ) {
        this.playerReader = playerReader;
        this.playerFinder = playerFinder;
        this.playerRenewer = playerRenewer;
    }

    public Player get(BrawlStarsTag tag) {
        return playerReader.get(tag);
    }

    public List<PlayerBrawler> getBrawlers(BrawlStarsTag tag) {
        return playerReader.getBrawlers(playerReader.get(tag));
    }

    public void renew(BrawlStarsTag tag) {
        Player player = playerFinder.find(tag)
                .orElseThrow(() -> new CoreException(CoreErrorType.PLAYER_NOT_FOUND, "playerTag=" + tag));
        playerRenewer.renew(player);
    }

    public void renewNew(BrawlStarsTag tag) {
        playerRenewer.renewNew(tag);
    }

    public boolean isRenewing(BrawlStarsTag tag) {
        return playerRenewer.isRenewing(tag);
    }

    public boolean isRenewingNew(BrawlStarsTag tag) {
        return playerRenewer.isRenewingNew(tag);
    }
}

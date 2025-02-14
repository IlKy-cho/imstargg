package com.imstargg.core.domain;

import com.imstargg.core.error.CoreErrorType;
import com.imstargg.core.error.CoreException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    private final PlayerReader playerReader;
    private final PlayerFinder playerFinder;
    private final PlayerRenewalExecutor playerRenewalExecutor;

    public PlayerService(
            PlayerReader playerReader,
            PlayerFinder playerFinder,
            PlayerRenewalExecutor playerRenewalExecutor
    ) {
        this.playerReader = playerReader;
        this.playerFinder = playerFinder;
        this.playerRenewalExecutor = playerRenewalExecutor;
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
        playerRenewalExecutor.renew(player);
    }

    public void renewNew(BrawlStarsTag tag) {
        if (playerFinder.find(tag).isPresent()) {
            return;
        }
        playerRenewalExecutor.renewNew(tag);
    }

    public boolean isRenewing(BrawlStarsTag tag) {
        return playerRenewalExecutor.isRenewing(playerReader.get(tag));
    }

    public boolean isRenewingNew(BrawlStarsTag tag) {
        return playerRenewalExecutor.isRenewingNew(tag);
    }

}

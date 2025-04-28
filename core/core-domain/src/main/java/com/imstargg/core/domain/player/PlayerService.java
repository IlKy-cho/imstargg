package com.imstargg.core.domain.player;

import com.imstargg.core.domain.BrawlStarsTag;
import com.imstargg.core.error.CoreErrorType;
import com.imstargg.core.error.CoreException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    private final PlayerReader playerReader;
    private final PlayerRenewalExecutor playerRenewalExecutor;

    public PlayerService(
            PlayerReader playerReader,
            PlayerRenewalExecutor playerRenewalExecutor
    ) {
        this.playerReader = playerReader;
        this.playerRenewalExecutor = playerRenewalExecutor;
    }

    public Player get(BrawlStarsTag tag) {
        return playerReader.get(tag);
    }

    public List<PlayerBrawler> getBrawlers(BrawlStarsTag tag) {
        return playerReader.getBrawlers(playerReader.get(tag));
    }

    public void renew(BrawlStarsTag tag) {
        Player player = playerReader.get(tag);
        playerRenewalExecutor.renew(player);
    }

    public void renewNew(BrawlStarsTag tag) {
        if (!tag.isValid()) {
            throw new CoreException(CoreErrorType.BRAWLSTARS_INVALID_TAG, "tag=" + tag);
        }
        UnknownPlayer unknownPlayer = playerReader.getUnknown(tag);
        playerRenewalExecutor.renewNew(unknownPlayer);
    }

    public boolean isRenewing(BrawlStarsTag tag) {
        return playerRenewalExecutor.isRenewing(playerReader.get(tag));
    }

    public boolean isRenewingNew(BrawlStarsTag tag) {
        return playerRenewalExecutor.isRenewingNew(tag);
    }

}

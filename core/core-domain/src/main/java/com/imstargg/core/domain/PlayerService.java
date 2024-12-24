package com.imstargg.core.domain;

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
        playerFinder.find(tag).ifPresentOrElse(
                playerRenewer::renew,
                () -> playerRenewer.renewNew(tag)
        );
    }

    public boolean isRenewing(BrawlStarsTag tag) {
        return playerRenewer.isRenewing(tag);
    }
}

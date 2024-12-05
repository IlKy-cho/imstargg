package com.imstargg.core.domain;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    private final PlayerReader playerReader;
    private final PlayerRenewer playerRenewer;

    public PlayerService(PlayerReader playerReader, PlayerRenewer playerRenewer) {
        this.playerReader = playerReader;
        this.playerRenewer = playerRenewer;
    }

    public Player get(BrawlStarsTag tag) {
        return playerReader.get(tag);
    }

    public List<PlayerBrawler> getBrawlers(BrawlStarsTag tag) {
        return playerReader.getBrawlers(playerReader.get(tag));
    }

    public void renew(BrawlStarsTag tag) {
        Player player = playerReader.get(tag);
        playerRenewer.renew(player);
    }

    public boolean isRenewalFinished(BrawlStarsTag tag) {
        Player player = playerReader.get(tag);
        return !player.status().isRenewing();
    }
}

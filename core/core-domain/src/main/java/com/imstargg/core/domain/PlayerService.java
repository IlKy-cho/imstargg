package com.imstargg.core.domain;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    private final PlayerReader playerReader;

    public PlayerService(PlayerReader playerReader) {
        this.playerReader = playerReader;
    }

    public Player get(BrawlStarsTag tag) {
        return playerReader.get(tag);
    }

    public List<PlayerBrawler> getBrawlers(BrawlStarsTag tag) {
        return playerReader.getBrawlers(playerReader.get(tag));
    }
}

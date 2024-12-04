package com.imstargg.core.domain;

import org.springframework.stereotype.Service;

@Service
public class PlayerService {

    private final PlayerReader playerReader;

    public PlayerService(PlayerReader playerReader) {
        this.playerReader = playerReader;
    }

    public Player get(BrawlStarsTag tag) {
        return playerReader.get(tag);
    }
}

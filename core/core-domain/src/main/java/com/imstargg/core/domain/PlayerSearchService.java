package com.imstargg.core.domain;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerSearchService {

    private final PlayerSearcher playerSearcher;

    public PlayerSearchService(
            PlayerSearcher playerSearcher
    ) {
        this.playerSearcher = playerSearcher;
    }

    public List<Player> search(PlayerSearchParam param) {
        return playerSearcher.searchName(param.query());
    }
}

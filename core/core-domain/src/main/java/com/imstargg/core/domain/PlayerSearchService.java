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
        if (!param.isTag()) {
            return playerSearcher.searchName(param.query());
        }

        return playerSearcher.searchTag(param.toTag());
    }
}

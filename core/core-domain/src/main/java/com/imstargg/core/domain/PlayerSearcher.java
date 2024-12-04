package com.imstargg.core.domain;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlayerSearcher {

    private final PlayerRepository playerRepository;

    public PlayerSearcher(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> searchTag(BrawlStarsTag tag) {
        return playerRepository.findByTag(tag)
                .map(List::of)
                .orElseGet(List::of);
    }

    public List<Player> searchName(String name) {
        return playerRepository.findByName(name);
    }
}

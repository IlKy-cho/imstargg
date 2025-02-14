package com.imstargg.core.domain;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PlayerSearchService {

    private final PlayerFinder playerFinder;
    private final PlayerSearcher playerSearcher;

    public PlayerSearchService(
            PlayerFinder playerFinder,
            PlayerSearcher playerSearcher
    ) {
        this.playerFinder = playerFinder;
        this.playerSearcher = playerSearcher;
    }

    public List<Player> search(PlayerSearchParam param) {
        List<Player> result = new ArrayList<>();
        BrawlStarsTag tag = new BrawlStarsTag(param.query());
        if (tag.isValid()) {
            playerFinder.find(tag).ifPresent(result::add);
        }
        result.addAll(playerSearcher.searchName(param.query()));
        return Collections.unmodifiableList(result);
    }
}

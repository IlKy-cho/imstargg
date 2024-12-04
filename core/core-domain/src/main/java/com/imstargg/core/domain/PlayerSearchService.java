package com.imstargg.core.domain;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerSearchService {

    private final PlayerSearcher playerSearcher;
    private final PlayerRefresher playerRefresher;

    public PlayerSearchService(
            PlayerSearcher playerSearcher,
            PlayerRefresher playerRefresher
    ) {
        this.playerSearcher = playerSearcher;
        this.playerRefresher = playerRefresher;
    }

    public PlayerSearchResult search(PlayerSearchParam param) {
        if (!param.isTag()) {
            List<Player> players = playerSearcher.searchName(param.query());
            return PlayerSearchResult.nonAcceptedResult(players);
        }

        BrawlStarsTag tag = param.toTag();
        List<Player> players = playerSearcher.searchTag(tag);
        if (!players.isEmpty()) {
            return PlayerSearchResult.nonAcceptedResult(players);
        }

        return playerRefresher.refreshNew(tag)
                ? PlayerSearchResult.acceptedResult(tag) : PlayerSearchResult.nonAcceptedResult(List.of());
    }
}

package com.imstargg.core.domain;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerSearchService {

    private final PlayerSearcher playerSearcher;
    private final PlayerRenewer playerRenewer;

    public PlayerSearchService(
            PlayerSearcher playerSearcher,
            PlayerRenewer playerRenewer
    ) {
        this.playerSearcher = playerSearcher;
        this.playerRenewer = playerRenewer;
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

        return playerRenewer.renewNew(tag)
                ? PlayerSearchResult.acceptedResult(tag) : PlayerSearchResult.nonAcceptedResult(List.of());
    }
}

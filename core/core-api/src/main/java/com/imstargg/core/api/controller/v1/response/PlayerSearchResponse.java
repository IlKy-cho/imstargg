package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.Player;

import java.util.List;

public record PlayerSearchResponse(
        List<PlayerResponse> players
) {

    public static PlayerSearchResponse from(List<Player> players) {
        return new PlayerSearchResponse(
                players.stream()
                        .map(PlayerResponse::from)
                        .toList()
        );
    }
}

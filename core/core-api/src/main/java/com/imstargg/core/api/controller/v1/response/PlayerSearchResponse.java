package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.PlayerSearchResult;
import jakarta.annotation.Nullable;

import java.util.List;

public record PlayerSearchResponse(
        List<PlayerResponse> players,
        @Nullable String acceptedTag
) {

    public static PlayerSearchResponse from(PlayerSearchResult playerSearchResult) {
        return new PlayerSearchResponse(
                playerSearchResult.players().stream()
                        .map(PlayerResponse::from)
                        .toList(),
                playerSearchResult.acceptedTag() != null ? playerSearchResult.acceptedTag().value() : null
        );
    }
}

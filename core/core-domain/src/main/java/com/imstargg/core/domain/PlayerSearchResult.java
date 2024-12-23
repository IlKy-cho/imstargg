package com.imstargg.core.domain;

import jakarta.annotation.Nullable;

import java.util.List;

public record PlayerSearchResult(
        List<Player> players,
        @Nullable BrawlStarsTag acceptedTag
) {

    public static PlayerSearchResult accepted(BrawlStarsTag tag) {
        return new PlayerSearchResult(List.of(), tag);
    }

    public static PlayerSearchResult notAccepted(List<Player> players) {
        return new PlayerSearchResult(players, null);
    }

    public static PlayerSearchResult empty() {
        return new PlayerSearchResult(List.of(), null);
    }

    public boolean isAccepted() {
        return acceptedTag != null;
    }
}

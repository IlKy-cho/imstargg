package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.ranking.PlayerRanking;
import jakarta.annotation.Nullable;

public record PlayerRankingResponse(
        String tag,
        String name,
        @Nullable String nameColor,
        @Nullable String clubName,
        long iconId,
        int trophies,
        int rank,
        @Nullable Integer rankChange
) {

    public static PlayerRankingResponse of(PlayerRanking playerRanking) {
        return new PlayerRankingResponse(
                playerRanking.tag().value(),
                playerRanking.name(),
                playerRanking.nameColor(),
                playerRanking.clubName(),
                playerRanking.iconId().value(),
                playerRanking.trophies(),
                playerRanking.rank(),
                playerRanking.rankChange()
        );
    }
}

package com.imstargg.client.brawlstars.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PlayerResponse(
        String tag,
        String name,
        String nameColor,
        PlayerIconResponse icon,
        int trophies,
        int highestTrophies,
        int expLevel,
        int expPoints,
        boolean isQualifiedFromChampionshipChallenge,
        @JsonProperty("3vs3Victories") int victories3vs3,
        int soloVictories,
        int duoVictories,
        int bestRoboRumbleTime,
        int bestTimeAsBigBrawler,
        PlayerClubResponse club,
        List<BrawlerStatResponse> brawlers
) {
}

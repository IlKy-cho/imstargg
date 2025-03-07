package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.player.PlayerBrawler;

import java.util.List;

public record PlayerBrawlerResponse(
        long brawlerId,
        List<Long> gearIds,
        List<Long> starPowerIds,
        List<Long> gadgetIds,
        int power,
        int rank,
        int trophies,
        int highestTrophies
) {

    public static PlayerBrawlerResponse from(PlayerBrawler playerBrawler) {
        return new PlayerBrawlerResponse(
                playerBrawler.brawlStarsId().value(),
                playerBrawler.gearIds().stream().map(BrawlStarsId::value).toList(),
                playerBrawler.starPowerIds().stream().map(BrawlStarsId::value).toList(),
                playerBrawler.gadgetIds().stream().map(BrawlStarsId::value).toList(),
                playerBrawler.power(),
                playerBrawler.rank(),
                playerBrawler.trophies(),
                playerBrawler.highestTrophies()
        );
    }
}

package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.PlayerBrawler;

import java.util.List;

public record PlayerBrawlerResponse(
        BrawlerResponse brawler,
        List<GearResponse> gears,
        List<StarPowerResponse> starPowers,
        List<GadgetResponse> gadgets,
        int power,
        int rank,
        int trophies,
        int highestTrophies
) {

    public static PlayerBrawlerResponse from(PlayerBrawler playerBrawler) {
        return new PlayerBrawlerResponse(
                playerBrawler.brawler() == null ? null : BrawlerResponse.from(playerBrawler.brawler()),
                playerBrawler.gears().stream().map(GearResponse::from).toList(),
                playerBrawler.starPowers().stream().map(StarPowerResponse::from).toList(),
                playerBrawler.gadgets().stream().map(GadgetResponse::from).toList(),
                playerBrawler.power(),
                playerBrawler.rank(),
                playerBrawler.trophies(),
                playerBrawler.highestTrophies()
        );
    }
}

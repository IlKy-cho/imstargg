package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.PlayerBrawler;
import com.imstargg.core.enums.Gadget;
import com.imstargg.core.enums.Gear;
import com.imstargg.core.enums.StarPower;

import java.util.List;

public record PlayerBrawlerResponse(
        BrawlerResponse brawler,
        List<Gear> gears,
        List<StarPower> starPowers,
        List<Gadget> gadgets,
        int power,
        int rank,
        int trophies,
        int highestTrophies
) {

    public static PlayerBrawlerResponse from(PlayerBrawler playerBrawler) {
        return new PlayerBrawlerResponse(
                playerBrawler.brawler() == null ? null : BrawlerResponse.from(playerBrawler.brawler()),
                playerBrawler.gears(),
                playerBrawler.starPowers(),
                playerBrawler.gadgets(),
                playerBrawler.power(),
                playerBrawler.rank(),
                playerBrawler.trophies(),
                playerBrawler.highestTrophies()
        );
    }
}

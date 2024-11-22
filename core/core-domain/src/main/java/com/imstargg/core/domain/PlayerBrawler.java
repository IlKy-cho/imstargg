package com.imstargg.core.domain;

import java.util.List;

public record PlayerBrawler(
        Brawler brawler,
        List<Gear> gears,
        List<StarPower> starPowers,
        List<Gadget> gadgets,
        int power,
        int rank,
        int trophies,
        int highestTrophies
) {
}

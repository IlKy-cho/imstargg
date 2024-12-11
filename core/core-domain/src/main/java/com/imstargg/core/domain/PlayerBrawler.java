package com.imstargg.core.domain;

import com.imstargg.core.domain.brawlstars.Brawler;
import com.imstargg.core.domain.brawlstars.Gadget;
import com.imstargg.core.domain.brawlstars.Gear;
import com.imstargg.core.domain.brawlstars.StarPower;
import jakarta.annotation.Nullable;

import java.util.List;

public record PlayerBrawler(
        @Nullable Brawler brawler,
        List<Gear> gears,
        List<StarPower> starPowers,
        List<Gadget> gadgets,
        int power,
        int rank,
        int trophies,
        int highestTrophies
) {
}

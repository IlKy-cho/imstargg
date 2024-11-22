package com.imstargg.core.domain;

import java.util.List;

public record Brawler(
        BrawlStarsId id,
        String name,
        List<Gear> gears,
        List<StarPower> starPowers,
        List<Gadget> gadgets
) {
}

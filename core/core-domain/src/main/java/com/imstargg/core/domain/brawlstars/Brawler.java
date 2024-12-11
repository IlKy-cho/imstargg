package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.enums.BrawlerRarity;

import java.util.List;

public record Brawler(
        BrawlStarsId id,
        String name,
        BrawlerRarity rarity,
        List<Gadget> gadgets,
        List<Gear> gears,
        List<StarPower> starPowers
) {

    public List<Gadget> filterGadgets(List<BrawlStarsId> ids) {
        return gadgets.stream()
                .filter(gadget -> ids.contains(gadget.id()))
                .toList();
    }

    public List<Gear> filterGears(List<BrawlStarsId> ids) {
        return gears.stream()
                .filter(gear -> ids.contains(gear.id()))
                .toList();
    }

    public List<StarPower> filterStarPowers(List<BrawlStarsId> ids) {
        return starPowers.stream()
                .filter(starPower -> ids.contains(starPower.id()))
                .toList();
    }
}

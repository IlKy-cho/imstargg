package com.imstargg.core.domain.statistics.brawler;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.brawlstars.Brawler;
import com.imstargg.core.domain.statistics.ItemRate;
import org.springframework.stereotype.Component;

@Component
public class BrawlerOwnershipReaderWithCache {

    private final BrawlerCountRepository brawlerCountRepository;
    private final BrawlerOwnershipRateCache cache;

    public BrawlerOwnershipReaderWithCache(
            BrawlerCountRepository brawlerCountRepository,
            BrawlerOwnershipRateCache cache
    ) {
        this.brawlerCountRepository = brawlerCountRepository;
        this.cache = cache;
    }

    public BrawlerItemOwnership get(Brawler brawler) {
        return cache.get(brawler.id(), () -> load(brawler));
    }

    public BrawlerItemOwnership load(Brawler brawler) {
        int brawlerCount = brawlerCountRepository.getBrawlerCount(brawler);
        return new BrawlerItemOwnership(
                brawler.gadgets().stream().map(gadget ->
                        rate(gadget.id(), brawlerCount, brawlerCountRepository.getGadgetCount(brawler, gadget))
                ).toList(),
                brawler.starPowers().stream().map(starPower ->
                        rate(starPower.id(), brawlerCount, brawlerCountRepository.getStarPowerCount(brawler, starPower))
                ).toList(),
                brawler.gears().stream().map(gear ->
                        rate(gear.id(), brawlerCount, brawlerCountRepository.getGearCount(brawler, gear))
                ).toList()
        );
    }

    private ItemRate rate(BrawlStarsId id, int totalCount, int count) {
        if (totalCount == 0) {
            return new ItemRate(id, 0.0);
        }

        return new ItemRate(id, (double) count / totalCount);
    }
}

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

    public BrawlerItemOwnership get(Brawler brawler, TrophyRangeRange trophyRangeRange) {
        return cache.get(brawler.id(), trophyRangeRange, () -> load(brawler, trophyRangeRange));
    }

    private BrawlerItemOwnership load(Brawler brawler, TrophyRangeRange trophyRangeRange) {
        int brawlerCount = trophyRangeRange.getRanges().stream()
                .mapToInt(range -> brawlerCountRepository.getBrawlerCount(brawler, range))
                .sum();
        return new BrawlerItemOwnership(
                brawler.gadgets().stream().map(gadget ->
                        rate(
                                gadget.id(),
                                brawlerCount,
                                trophyRangeRange.getRanges().stream().mapToInt(range ->
                                        brawlerCountRepository.getGadgetCount(brawler, gadget, range)
                                ).sum()
                        )
                ).toList(),
                brawler.starPowers().stream().map(starPower ->
                        rate(
                                starPower.id(),
                                brawlerCount,
                                trophyRangeRange.getRanges().stream().mapToInt(range ->
                                        brawlerCountRepository.getStarPowerCount(brawler, starPower, range)
                                ).sum()
                        )
                ).toList(),
                brawler.gears().stream().map(gear ->
                        rate(
                                gear.id(),
                                brawlerCount,
                                trophyRangeRange.getRanges().stream().mapToInt(range ->
                                        brawlerCountRepository.getGearCount(brawler, gear, range)
                                ).sum()
                        )
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

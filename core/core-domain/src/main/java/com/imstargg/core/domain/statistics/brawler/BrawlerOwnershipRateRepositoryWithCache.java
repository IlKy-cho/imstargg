package com.imstargg.core.domain.statistics.brawler;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.brawlstars.Brawler;
import com.imstargg.core.domain.statistics.ItemRate;
import com.imstargg.storage.db.core.cache.BrawlerCountCacheKey;
import com.imstargg.storage.db.core.cache.GadgetCountCacheKey;
import com.imstargg.storage.db.core.cache.PlayerCountCacheKey;
import com.imstargg.storage.db.core.cache.RdsCacheEntity;
import com.imstargg.storage.db.core.cache.RdsCacheJpaRepository;
import com.imstargg.storage.db.core.cache.StarPowerCountCacheKey;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BrawlerOwnershipRateRepositoryWithCache {

    private final RdsCacheJpaRepository rdsCacheJpaRepository;
    private final BrawlerOwnershipRateCache brawlerOwnershipRateCache;

    public BrawlerOwnershipRateRepositoryWithCache(
            RdsCacheJpaRepository rdsCacheJpaRepository,
            BrawlerOwnershipRateCache brawlerOwnershipRateCache
    ) {
        this.rdsCacheJpaRepository = rdsCacheJpaRepository;
        this.brawlerOwnershipRateCache = brawlerOwnershipRateCache;
    }

    public BrawlerOwnershipRate get(Brawler brawler) {
        return brawlerOwnershipRateCache.get(brawler.id(), () -> load(brawler));
    }

    private BrawlerOwnershipRate load(Brawler brawler) {
        Long playerCount = rdsCacheJpaRepository.findByKey(PlayerCountCacheKey.KEY)
                .map(RdsCacheEntity::getValue)
                .map(Long::parseLong)
                .orElse(null);
        Long brawlerCount = rdsCacheJpaRepository.findByKey(new BrawlerCountCacheKey(brawler.id().value()).key())
                .map(RdsCacheEntity::getValue)
                .map(Long::parseLong)
                .orElse(null);
        return new BrawlerOwnershipRate(
                rate(brawler.id(), playerCount, brawlerCount),
                brawler.gadgets().stream().map(gadget ->
                        rdsCacheJpaRepository.findByKey(new GadgetCountCacheKey(gadget.id().value()).key())
                                .map(RdsCacheEntity::getValue)
                                .map(Long::parseLong)
                                .map(count -> rate(gadget.id(), brawlerCount, count))
                ).filter(Optional::isPresent).map(Optional::get).toList(),
                brawler.starPowers().stream().map(starPower ->
                        rdsCacheJpaRepository.findByKey(new StarPowerCountCacheKey(starPower.id().value()).key())
                                .map(RdsCacheEntity::getValue)
                                .map(Long::parseLong)
                                .map(count -> rate(starPower.id(), brawlerCount, count))
                ).filter(Optional::isPresent).map(Optional::get).toList(),
                brawler.gears().stream().map(gear ->
                        rdsCacheJpaRepository.findByKey(new GadgetCountCacheKey(gear.id().value()).key())
                                .map(RdsCacheEntity::getValue)
                                .map(Long::parseLong)
                                .map(count -> rate(gear.id(), brawlerCount, count))
                ).filter(Optional::isPresent).map(Optional::get).toList()
        );
    }

    @Nullable
    private ItemRate rate(BrawlStarsId id, @Nullable Long totalCount, @Nullable Long count) {
        if (totalCount == null || totalCount == 0L || count == null) {
            return null;
        }

        return new ItemRate(id, (double) count / totalCount);
    }
}

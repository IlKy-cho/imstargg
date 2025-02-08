package com.imstargg.storage.cache.core;

public record GearCountCacheKey(
        long brawlerBrawlStarsId,
        long gearBrawlStarsId
) implements CacheKey {

    @Override
    public String key() {
        return new CacheKeyBuilder("count", "v1")
                .add("brawlers").add(brawlerBrawlStarsId)
                .add("gears").add(gearBrawlStarsId)
                .build();
    }
}

package com.imstargg.storage.cache.core;

public record BrawlerCountCacheKey(
        long brawlStarsId
) implements CacheKey {

    @Override
    public String key() {
        return new CacheKeyBuilder("count", "v1")
                .add("brawlers").add(brawlStarsId)
                .build();
    }
}

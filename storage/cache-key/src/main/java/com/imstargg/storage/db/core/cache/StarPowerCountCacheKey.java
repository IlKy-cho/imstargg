package com.imstargg.storage.db.core.cache;

public record StarPowerCountCacheKey(
        long brawlStarsId
) implements CacheKey {

    @Override
    public String key() {
        return new CacheKeyBuilder("count", "v1")
                .add("starpowers").add(brawlStarsId)
                .build();
    }
}

package com.imstargg.storage.db.core.cache;

public record GadgetCountCacheKey(
        long brawlStarsId
) implements CacheKey {

    @Override
    public String key() {
        return new CacheKeyBuilder("count", "v1")
                .add("gadgets").add(brawlStarsId)
                .build();
    }
}

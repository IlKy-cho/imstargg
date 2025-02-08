package com.imstargg.storage.db.core.cache;

public abstract class PlayerCountCacheKey {

    public static final String KEY = new CacheKeyBuilder("count", "v1")
            .add("player")
            .build();

    private PlayerCountCacheKey() {
        throw new UnsupportedOperationException();
    }
}

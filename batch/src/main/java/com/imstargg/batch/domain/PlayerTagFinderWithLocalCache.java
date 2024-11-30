package com.imstargg.batch.domain;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.imstargg.storage.db.core.PlayerCollectionJpaRepository;
import com.imstargg.storage.db.core.UnknownPlayerCollectionJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class PlayerTagFinderWithLocalCache {

    private final Cache<String, Boolean> tagCache = Caffeine.newBuilder()
            .maximumSize(1000)
            .build();

    private final PlayerCollectionJpaRepository playerRepository;
    private final UnknownPlayerCollectionJpaRepository unknownPlayerRepository;

    public PlayerTagFinderWithLocalCache(
            PlayerCollectionJpaRepository playerRepository,
            UnknownPlayerCollectionJpaRepository unknownPlayerRepository
    ) {
        this.playerRepository = playerRepository;
        this.unknownPlayerRepository = unknownPlayerRepository;
    }

    public boolean exists(String tag) {
        Boolean cacheResult = tagCache.getIfPresent(tag);
        if (cacheResult != null) {
            return true;
        }

        tagCache.put(tag, true);
        return playerRepository.existsByBrawlStarsTag(tag) || unknownPlayerRepository.existsByBrawlStarsTag(tag);
    }
}

package com.imstargg.batch.domain;

import com.imstargg.storage.db.core.PlayerCollectionJpaRepository;
import com.imstargg.storage.db.core.UnknownPlayerCollectionJpaRepository;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentSkipListSet;

@Component
public class PlayerTagFinderWithLocalCache {

    private final ConcurrentSkipListSet<String> tagCache = new ConcurrentSkipListSet<>();

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
        if (tagCache.contains(tag)) {
            return true;
        }

        tagCache.add(tag);
        return playerRepository.existsByBrawlStarsTag(tag) || unknownPlayerRepository.existsByBrawlStarsTag(tag);
    }
}

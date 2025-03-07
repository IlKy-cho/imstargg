package com.imstargg.core.domain.brawlstars;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.imstargg.core.domain.BrawlStarsId;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
public class BattleEventCache {

    private final Cache<BrawlStarsId, BattleEvent> caffeine = Caffeine.newBuilder()
            .maximumSize(50)
            .expireAfterWrite(Duration.ofHours(1))
            .build();

    public void set(BrawlStarsId id, BattleEvent battleEvent) {
        caffeine.put(id, battleEvent);
    }

    public Optional<BattleEvent> find(BrawlStarsId id) {
        return Optional.ofNullable(caffeine.getIfPresent(id));
    }
}

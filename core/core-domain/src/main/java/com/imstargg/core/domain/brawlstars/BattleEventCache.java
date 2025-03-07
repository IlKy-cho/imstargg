package com.imstargg.core.domain.brawlstars;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.enums.Language;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
public class BattleEventCache {

    private final Cache<Key, BattleEvent> caffeine = Caffeine.newBuilder()
            .maximumSize(50)
            .expireAfterWrite(Duration.ofHours(1))
            .build();

    public void set(BrawlStarsId id, Language language, BattleEvent battleEvent) {
        caffeine.put(new Key(id, language), battleEvent);
    }

    public Optional<BattleEvent> find(BrawlStarsId id, Language language) {
        return Optional.ofNullable(caffeine.getIfPresent(new Key(id, language)));
    }

    private record Key(BrawlStarsId id, Language language) {
    }
}

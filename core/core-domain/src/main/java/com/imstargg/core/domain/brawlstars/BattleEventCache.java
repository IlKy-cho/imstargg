package com.imstargg.core.domain.brawlstars;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.support.ObjectMapperHelper;
import com.imstargg.storage.db.core.cache.CacheKeyBuilder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Component
public class BattleEventCache {

    private final Cache<BrawlStarsId, BattleEvent> caffeine = Caffeine.newBuilder()
            .maximumSize(50)
            .expireAfterWrite(Duration.ofHours(1))
            .build();
    private final ObjectMapperHelper objectMapperHelper;
    private final StringRedisTemplate redisTemplate;

    public BattleEventCache(ObjectMapperHelper objectMapperHelper, StringRedisTemplate redisTemplate) {
        this.objectMapperHelper = objectMapperHelper;
        this.redisTemplate = redisTemplate;
    }

    public void set(BrawlStarsId id, BattleEvent battleEvent) {
        caffeine.put(id, battleEvent);
    }

    public Optional<BattleEvent> find(BrawlStarsId id) {
        return Optional.ofNullable(caffeine.getIfPresent(id));
    }

    public List<BrawlStarsId> getSoloRankEventIds(Supplier<List<BrawlStarsId>> mappingFunction) {
        String key = new CacheKeyBuilder("event", "v1")
                .add("solo-rank-event-ids")
                .build();
        return Optional.ofNullable(
                        redisTemplate.opsForValue().get(key)
                ).map(value -> objectMapperHelper.read(value, new TypeReference<List<Long>>() {
                }))
                .orElseGet(() -> {
                    List<Long> soloRankEventIds = mappingFunction.get().stream()
                            .map(BrawlStarsId::value)
                            .toList();
                    redisTemplate.opsForValue()
                            .set(key, objectMapperHelper.write(soloRankEventIds), Duration.ofHours(1));
                    return soloRankEventIds;
                }).stream()
                .map(BrawlStarsId::new)
                .toList();
    }
}

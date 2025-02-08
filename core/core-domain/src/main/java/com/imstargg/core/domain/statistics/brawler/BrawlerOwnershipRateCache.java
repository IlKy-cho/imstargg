package com.imstargg.core.domain.statistics.brawler;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.support.ObjectMapperHelper;
import com.imstargg.storage.db.core.cache.CacheKeyBuilder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.function.Supplier;

@Component
public class BrawlerOwnershipRateCache {

    private static final Duration TTL = Duration.ofHours(1);

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapperHelper objectMapper;

    public BrawlerOwnershipRateCache(StringRedisTemplate redisTemplate, ObjectMapperHelper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public BrawlerOwnershipRate get(
            BrawlStarsId brawlerId, Supplier<BrawlerOwnershipRate> loader
    ) {
        String cacheValue = redisTemplate.opsForValue().get(key(brawlerId));
        if (cacheValue != null) {
            return objectMapper.read(cacheValue, BrawlerOwnershipRate.class);
        }
        BrawlerOwnershipRate value = loader.get();
        redisTemplate.opsForValue().set(key(brawlerId), objectMapper.write(value), TTL);
        return value;
    }

    private String key(BrawlStarsId brawlerId) {
        return new CacheKeyBuilder("ownership", "v1")
                .add("brawlers").add(brawlerId.value())
                .build();
    }
}

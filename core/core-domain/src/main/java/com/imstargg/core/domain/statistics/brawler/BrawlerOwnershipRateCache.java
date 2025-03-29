package com.imstargg.core.domain.statistics.brawler;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.brawlstars.Brawler;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.core.support.ObjectMapperHelper;
import com.imstargg.storage.db.core.cache.CacheKeyBuilder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;
import java.util.function.BiFunction;

@Component
public class BrawlerOwnershipRateCache {

    private static final Duration TTL = Duration.ofHours(1);
    private static final String VERSION = "v2";
    private static final String BRAWLER_CACHE_KEY = "brawler";
    private static final String TROPHY_RANGE_CACHE_KEY = "trophy-range";

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapperHelper objectMapper;

    public BrawlerOwnershipRateCache(StringRedisTemplate redisTemplate, ObjectMapperHelper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public BrawlerItemOwnership get(
            Brawler brawler,
            TrophyRange trophyRange,
            BiFunction<BrawlStarsId, TrophyRange, BrawlerItemOwnership> mappingFunction
    ) {
        return Optional.ofNullable(
                        redisTemplate.opsForValue().get(key(brawler.id(), trophyRange))
                ).map(value -> objectMapper.read(value, BrawlerItemOwnership.class))
                .orElseGet(() -> {
                    BrawlerItemOwnership ownership = mappingFunction.apply(brawler.id(), trophyRange);
                    redisTemplate.opsForValue()
                            .set(key(brawler.id(), trophyRange), objectMapper.write(ownership), TTL);
                    return ownership;
                });
    }

    private String key(BrawlStarsId brawlerId, TrophyRange trophyRange) {
        return new CacheKeyBuilder("brawler-ownership", VERSION)
                .add(BRAWLER_CACHE_KEY).add(brawlerId.value())
                .add(TROPHY_RANGE_CACHE_KEY).add(trophyRange)
                .build();
    }
}

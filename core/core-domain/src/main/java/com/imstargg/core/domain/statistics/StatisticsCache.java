package com.imstargg.core.domain.statistics;

import com.fasterxml.jackson.core.type.TypeReference;
import com.imstargg.core.domain.statistics.brawler.BrawlerBattleEventResultStatisticsParam;
import com.imstargg.core.domain.statistics.brawler.BrawlerBrawlersResultStatisticsParam;
import com.imstargg.core.domain.statistics.brawler.BrawlerEnemyResultStatisticsParam;
import com.imstargg.core.domain.statistics.brawler.BrawlerResultStatisticsParam;
import com.imstargg.core.domain.statistics.event.BattleEventBrawlerEnemyResultStatisticsParam;
import com.imstargg.core.domain.statistics.event.BattleEventBrawlerRankStatisticsParam;
import com.imstargg.core.domain.statistics.event.BattleEventBrawlerResultStatisticsParam;
import com.imstargg.core.domain.statistics.event.BattleEventBrawlerPairRankStatisticsParam;
import com.imstargg.core.domain.statistics.event.BattleEventBrawlerPairResultStatisticsParam;
import com.imstargg.core.support.ObjectMapperHelper;
import com.imstargg.storage.db.core.cache.CacheKeyBuilder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Component
public class StatisticsCache {

    private static final Duration TTL = Duration.ofHours(2);

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapperHelper objectMapper;

    public StatisticsCache(StringRedisTemplate redisTemplate, ObjectMapperHelper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    private String key(BrawlerResultStatisticsParam param) {
        return new CacheKeyBuilder("brawler-result-stats", "v1")
                .add("date").add(param.date())
                .add("dateRange").add(param.dateRange())
                .add("trophyRange").add(param.trophyRange())
                .add("soloRankTierRange").add(param.soloRankTierRange())
                .build();
    }

    public Optional<List<BrawlerResultStatistics>> find(BrawlerResultStatisticsParam param) {
        return Optional.ofNullable(
                redisTemplate.opsForValue().get(key(param))
        ).map(value -> objectMapper.read(value, new TypeReference<>() {
        }));
    }

    public void set(BrawlerResultStatisticsParam param, List<BrawlerResultStatistics> statistics) {
        redisTemplate.opsForValue().set(key(param), objectMapper.write(statistics), TTL);
    }

    private String key(BrawlerBattleEventResultStatisticsParam param) {
        return new CacheKeyBuilder("brawler-battle-event-result-stats", "v1")
                .add("date").add(param.date())
                .add("dateRange").add(param.dateRange())
                .add("trophyRange").add(param.trophyRange())
                .add("soloRankTierRange").add(param.soloRankTierRange())
                .build();
    }

    public Optional<List<BattleEventResultStatistics>> find(BrawlerBattleEventResultStatisticsParam param) {
        return Optional.ofNullable(
                redisTemplate.opsForValue().get(key(param))
        ).map(value -> objectMapper.read(value, new TypeReference<>() {
        }));
    }

    public void set(BrawlerBattleEventResultStatisticsParam param, List<BattleEventResultStatistics> statistics) {
        redisTemplate.opsForValue().set(key(param), objectMapper.write(statistics), TTL);
    }

    private String key(BrawlerBrawlersResultStatisticsParam param) {
        return new CacheKeyBuilder("brawler-brawlers-result-stats", "v1")
                .add("date").add(param.date())
                .add("dateRange").add(param.dateRange())
                .add("brawlerId").add(param.brawlerId().value())
                .add("trophyRange").add(param.trophyRange())
                .add("soloRankTierRange").add(param.soloRankTierRange())
                .add("brawlersNum").add(param.brawlersNum())
                .build();
    }

    public Optional<List<BrawlersResultStatistics>> find(BrawlerBrawlersResultStatisticsParam param) {
        return Optional.ofNullable(
                redisTemplate.opsForValue().get(key(param))
        ).map(value -> objectMapper.read(value, new TypeReference<>() {
        }));
    }

    public void set(BrawlerBrawlersResultStatisticsParam param, List<BrawlersResultStatistics> statistics) {
        redisTemplate.opsForValue().set(key(param), objectMapper.write(statistics), TTL);
    }

    private String key(BrawlerEnemyResultStatisticsParam param) {
        return new CacheKeyBuilder("brawler-enemy-result-stats", "v1")
                .add("date").add(param.date())
                .add("dateRange").add(param.dateRange())
                .add("brawlerId").add(param.brawlerId().value())
                .add("trophyRange").add(param.trophyRange())
                .add("soloRankTierRange").add(param.soloRankTierRange())
                .build();
    }

    public Optional<List<BrawlerEnemyResultStatistics>> find(BrawlerEnemyResultStatisticsParam param) {
        return Optional.ofNullable(
                redisTemplate.opsForValue().get(key(param))
        ).map(value -> objectMapper.read(value, new TypeReference<>() {
        }));
    }

    public void set(BrawlerEnemyResultStatisticsParam param, List<BrawlerEnemyResultStatistics> statistics) {
        redisTemplate.opsForValue().set(key(param), objectMapper.write(statistics), TTL);
    }

    private String key(BattleEventBrawlerResultStatisticsParam param) {
        return new CacheKeyBuilder("battle-event-brawler-result-stats", "v1")
                .add("event").add(param.eventId().value())
                .add("date").add(param.date())
                .add("dateRange").add(param.dateRange())
                .add("trophyRange").add(param.trophyRange())
                .add("soloRankTierRange").add(param.soloRankTierRange())
                .build();
    }

    public Optional<List<BrawlerResultStatistics>> find(BattleEventBrawlerResultStatisticsParam param) {
        return Optional.ofNullable(
                redisTemplate.opsForValue().get(key(param))
        ).map(value -> objectMapper.read(value, new TypeReference<>() {
        }));
    }

    public void set(BattleEventBrawlerResultStatisticsParam param, List<BrawlerResultStatistics> statistics) {
        redisTemplate.opsForValue().set(key(param), objectMapper.write(statistics), TTL);
    }

    private String key(BattleEventBrawlerPairResultStatisticsParam param) {
        return new CacheKeyBuilder("battle-event-brawlers-result-stats", "v1")
                .add("event").add(param.eventId().value())
                .add("brawlerId").add(param.brawlerId().value())
                .add("date").add(param.date())
                .add("dateRange").add(param.dateRange())
                .add("trophyRange").add(param.trophyRange())
                .add("soloRankTierRange").add(param.soloRankTierRange())
                .add("brawlersNum").add(param.brawlersNum())
                .build();
    }

    public Optional<List<BrawlersResultStatistics>> find(BattleEventBrawlerPairResultStatisticsParam param) {
        return Optional.ofNullable(
                redisTemplate.opsForValue().get(key(param))
        ).map(value -> objectMapper.read(value, new TypeReference<>() {
        }));
    }

    public void set(BattleEventBrawlerPairResultStatisticsParam param, List<BrawlersResultStatistics> statistics) {
        redisTemplate.opsForValue().set(key(param), objectMapper.write(statistics), TTL);
    }

    private String key(BattleEventBrawlerRankStatisticsParam param) {
        return new CacheKeyBuilder("battle-event-brawler-rank-stats", "v1")
                .add("event").add(param.eventId().value())
                .add("date").add(param.date())
                .add("dateRange").add(param.dateRange())
                .add("trophyRange").add(param.trophyRange().toString())
                .build();
    }

    public Optional<List<BrawlerRankStatistics>> find(BattleEventBrawlerRankStatisticsParam param) {
        return Optional.ofNullable(
                redisTemplate.opsForValue().get(key(param))
        ).map(value -> objectMapper.read(value, new TypeReference<>() {
        }));
    }

    public void set(BattleEventBrawlerRankStatisticsParam param, List<BrawlerRankStatistics> statistics) {
        redisTemplate.opsForValue().set(key(param), objectMapper.write(statistics), TTL);
    }

    private String key(BattleEventBrawlerPairRankStatisticsParam param) {
        return new CacheKeyBuilder("battle-event-brawlers-rank-stats", "v1")
                .add("event").add(param.eventId().value())
                .add("brawlerId").add(param.brawlerId().value())
                .add("date").add(param.date())
                .add("dateRange").add(param.dateRange())
                .add("trophyRange").add(param.trophyRangeRange().toString())
                .add("brawlersNum").add(param.brawlersNum())
                .build();
    }

    public Optional<List<BrawlersRankStatistics>> find(BattleEventBrawlerPairRankStatisticsParam param) {
        return Optional.ofNullable(
                redisTemplate.opsForValue().get(key(param))
        ).map(value -> objectMapper.read(value, new TypeReference<>() {
        }));
    }

    public void set(BattleEventBrawlerPairRankStatisticsParam param, List<BrawlersRankStatistics> statistics) {
        redisTemplate.opsForValue().set(key(param), objectMapper.write(statistics), TTL);
    }

    private String key(BattleEventBrawlerEnemyResultStatisticsParam param) {
        return new CacheKeyBuilder("battle-event-brawler-enemy-result-stats", "v1")
                .add("event").add(param.eventId().value())
                .add("brawlerId").add(param.brawlerId().value())
                .add("date").add(param.date())
                .add("dateRange").add(param.dateRange())
                .add("trophyRange").add(param.trophyRange())
                .add("soloRankTierRange").add(param.soloRankTierRange())
                .build();
    }

    public Optional<List<BrawlerEnemyResultStatistics>> find(BattleEventBrawlerEnemyResultStatisticsParam param) {
        return Optional.ofNullable(
                redisTemplate.opsForValue().get(key(param))
        ).map(value -> objectMapper.read(value, new TypeReference<>() {
        }));
    }

    public void set(BattleEventBrawlerEnemyResultStatisticsParam param, List<BrawlerEnemyResultStatistics> statistics) {
        redisTemplate.opsForValue().set(key(param), objectMapper.write(statistics), TTL);
    }
}

package com.imstargg.core.domain.statistics;

import com.fasterxml.jackson.core.type.TypeReference;
import com.imstargg.core.support.ObjectMapperHelper;
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

    private static String key(BrawlerResultStatisticsParam param) {
        return new CacheKeyBuilder("brawler-result-stats", "v1")
                .add("date", param.date())
                .add("trophyRange", param.trophyRange())
                .add("soloRankTierRange", param.soloRankTierRange())
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

    private static String key(BrawlerBattleEventResultStatisticsParam param) {
        return new CacheKeyBuilder("brawler-battle-event-result-stats", "v1")
                .add("date", param.date())
                .add("trophyRange", param.trophyRange())
                .add("soloRankTierRange", param.soloRankTierRange())
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

    private static String key(BrawlerBrawlersResultStatisticsParam param) {
        return new CacheKeyBuilder("brawler-brawlers-result-stats", "v1")
                .add("date", param.date())
                .add("brawlerId", param.brawlerId())
                .add("trophyRange", param.trophyRange())
                .add("soloRankTierRange", param.soloRankTierRange())
                .add("brawlersNum", param.brawlersNum())
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

    private static String key(BrawlerEnemyResultStatisticsParam param) {
        return new CacheKeyBuilder("brawler-enemy-result-stats", "v1")
                .add("date", param.date())
                .add("brawlerId", param.brawlerId())
                .add("trophyRange", param.trophyRange())
                .add("soloRankTierRange", param.soloRankTierRange())
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

    private static String key(BattleEventBrawlerResultStatisticsParam param) {
        return new CacheKeyBuilder("battle-event-brawler-result-stats", "v1")
                .add("eventId", param.eventId())
                .add("date", param.date())
                .add("trophyRange", param.trophyRangeRange())
                .add("soloRankTierRange", param.soloRankTierRangeRange())
                .add("duplicateBrawler", param.duplicateBrawler())
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

    private static String key(BattleEventBrawlersResultStatisticsParam param) {
        return new CacheKeyBuilder("battle-event-brawlers-result-stats", "v1")
                .add("eventId", param.eventId())
                .add("date", param.date())
                .add("trophyRange", param.trophyRangeRange())
                .add("soloRankTierRange", param.soloRankTierRangeRange())
                .add("brawlersNum", param.brawlersNum())
                .add("duplicateBrawler", param.duplicateBrawler())
                .build();
    }

    public Optional<List<BrawlersResultStatistics>> find(BattleEventBrawlersResultStatisticsParam param) {
        return Optional.ofNullable(
                redisTemplate.opsForValue().get(key(param))
        ).map(value -> objectMapper.read(value, new TypeReference<>() {
        }));
    }

    public void set(BattleEventBrawlersResultStatisticsParam param, List<BrawlersResultStatistics> statistics) {
        redisTemplate.opsForValue().set(key(param), objectMapper.write(statistics), TTL);
    }

    private static String key(BattleEventBrawlerRankStatisticsParam param) {
        return new CacheKeyBuilder("battle-event-brawler-rank-stats", "v1")
                .add("eventId", param.eventId())
                .add("date", param.date())
                .add("trophyRange", param.trophyRangeRange())
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

    private static String key(BattleEventBrawlersRankStatisticsParam param) {
        return new CacheKeyBuilder("battle-event-brawlers-rank-stats", "v1")
                .add("eventId", param.eventId())
                .add("date", param.date())
                .add("trophyRange", param.trophyRangeRange())
                .add("brawlersNum", param.brawlersNum())
                .build();
    }

    public Optional<List<BrawlersRankStatistics>> find(BattleEventBrawlersRankStatisticsParam param) {
        return Optional.ofNullable(
                redisTemplate.opsForValue().get(key(param))
        ).map(value -> objectMapper.read(value, new TypeReference<>() {
        }));
    }

    public void set(BattleEventBrawlersRankStatisticsParam param, List<BrawlersRankStatistics> statistics) {
        redisTemplate.opsForValue().set(key(param), objectMapper.write(statistics), TTL);
    }
}

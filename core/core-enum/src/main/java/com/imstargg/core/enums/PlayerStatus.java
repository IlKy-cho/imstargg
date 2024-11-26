package com.imstargg.core.enums;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public enum PlayerStatus {

    REFRESH_REQUESTED,
    UPDATING,
    UPDATED,
    DELETED,
    ;

    private static final Duration NEXT_UPDATABLE_TERM = Duration.ofSeconds(120);

    public boolean isUpdatable(Clock clock, LocalDateTime lastUpdatedAt) {
        if (DELETED == this) {
            return false;
        }
        return Duration.between(lastUpdatedAt, LocalDateTime.now(clock)).compareTo(NEXT_UPDATABLE_TERM) > 0;
    }

    public long nextUpdateWeight(
            Clock clock,
            List<PlayTime> playTimes,
            int expLevel,
            int playerTrophies,
            int brawlerTrophies
    ) {
        // TODO
        LocalDateTime now = LocalDateTime.now(clock);
        long timeDiffAvg = playTimes.stream()
                .map(PlayTime::battleTime)
                .mapToLong(battleTime -> Duration.between(battleTime, now).getSeconds())
                .sum() / playTimes.size();
        long durationAvg = playTimes.stream()
                .mapToLong(PlayTime::duration)
                .sum() / playTimes.size();

        return now.plusHours(1).toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public record PlayTime(LocalDateTime battleTime, int duration) {
    }
}

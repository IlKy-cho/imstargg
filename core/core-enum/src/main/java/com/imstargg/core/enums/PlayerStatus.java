package com.imstargg.core.enums;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public enum PlayerStatus {

    UPDATE_NEW,
    REFRESH_NEW,
    REFRESH_REQUESTED,
    UPDATED,
    DELETED,
    ;

    private static final Duration NEXT_UPDATABLE_TERM = Duration.ofSeconds(120);

    public boolean isUpdatable(Clock clock, LocalDateTime lastUpdatedAt) {
        if (UPDATE_NEW == this || REFRESH_NEW == this || REFRESH_REQUESTED == this) {
            return true;
        }
        if (DELETED == this) {
            return false;
        }
        return Duration.between(lastUpdatedAt, LocalDateTime.now(clock)).compareTo(NEXT_UPDATABLE_TERM) > 0;
    }

    public long initialUpdateWeight(Clock clock) {
        return switch (this) {
            case UPDATE_NEW -> LocalDateTime.now(clock).plusMinutes(30).toInstant(ZoneOffset.UTC).toEpochMilli();
            case REFRESH_NEW -> LocalDateTime.now(clock).toInstant(ZoneOffset.UTC).toEpochMilli();
            default -> throw new IllegalStateException("초기 업데이트 가중치를 계산할 수 없는 상태입니다. "
                    + getClass().getSimpleName() + ":" + this);
        };
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

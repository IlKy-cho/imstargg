package com.imstargg.core.domain.player;

import com.imstargg.core.domain.BrawlStarsTag;
import com.imstargg.core.enums.PlayerRenewalStatus;

import java.time.Clock;
import java.time.Duration;
import java.time.OffsetDateTime;

public record PlayerRenewal(
        BrawlStarsTag tag,
        PlayerRenewalStatus status,
        OffsetDateTime updatedAt
) {

    private static final Duration TERM = Duration.ofSeconds(120);
    private static final Duration TIMEOUT = Duration.ofSeconds(120);

    public boolean available(Player player, Clock clock) {
        if (!isCooldownOver(player, clock)) {
            return false;
        }

        return renewable(clock);
    }

    public boolean available(UnknownPlayer unknownPlayer, Clock clock) {
        if (!isCooldownOver(unknownPlayer, clock)) {
            return false;
        }

        return renewable(clock);
    }

    private boolean isCooldownOver(Player player, Clock clock) {
        return player.updatedAt().plus(TERM).isBefore(OffsetDateTime.now(clock));
    }

    private boolean isCooldownOver(UnknownPlayer unknownPlayer, Clock clock) {
        return OffsetDateTime.now(clock).isAfter(
                unknownPlayer.updatedAt().plus(TERM.multipliedBy(unknownPlayer.notFoundCount()))
        );
    }

    private boolean renewable(Clock clock) {
        if (!status.renewing()) {
            return true;
        }
        return OffsetDateTime.now(clock).isAfter(updatedAt.plus(TIMEOUT));
    }
}

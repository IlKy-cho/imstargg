package com.imstargg.core.domain;

import com.imstargg.core.enums.PlayerRenewalStatus;

import java.time.Clock;
import java.time.OffsetDateTime;

public record PlayerRenewal(
        BrawlStarsTag tag,
        PlayerRenewalStatus status,
        OffsetDateTime requestedAt
) {

    public boolean available(Player player, Clock clock) {
        if (!player.isNextUpdateCooldownOver(clock)) {
            return false;
        }

        return renewalAvailable(clock);
    }

    public boolean available(UnknownPlayer unknownPlayer, Clock clock) {
        if (!unknownPlayer.updateAvailable(clock)) {
            return false;
        }

        return renewalAvailable(clock);
    }

    private boolean renewalAvailable(Clock clock) {
        if (status.finished() || status == PlayerRenewalStatus.NEW) {
            return true;
        }
        return OffsetDateTime.now(clock).isAfter(requestedAt.plusMinutes(2));
    }
}

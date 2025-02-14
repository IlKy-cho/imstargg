package com.imstargg.core.domain;

import com.imstargg.core.enums.PlayerRenewalStatus;

import java.time.Clock;
import java.time.OffsetDateTime;

public record PlayerRenewal(
        BrawlStarsTag tag,
        PlayerRenewalStatus status,
        OffsetDateTime updatedAt
) {

    public boolean available(Player player, Clock clock) {
        if (!player.isNextUpdateCooldownOver(clock)) {
            return false;
        }

        if (status.finished()) {
            return true;
        }

        return OffsetDateTime.now(clock).isAfter(updatedAt.plusMinutes(2));
    }

    public boolean available(UnknownPlayer unknownPlayer, Clock clock) {
        if (!unknownPlayer.updateAvailable(clock)) {
            return false;
        }

        if (status.finished()) {
            return true;
        }

        return OffsetDateTime.now(clock).isAfter(updatedAt.plusMinutes(2));
    }
}

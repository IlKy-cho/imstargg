package com.imstargg.core.domain;

import com.imstargg.core.enums.PlayerRenewalStatus;

import java.time.Clock;
import java.time.LocalDateTime;

public record PlayerRenewal(
        PlayerRenewalStatus status,
        LocalDateTime updatedAt
) {

    public boolean available(Player player, Clock clock) {
        if (!player.isNextUpdateCooldownOver(clock)) {
            return false;
        }

        if (status.finished()) {
            return true;
        }

        return LocalDateTime.now(clock).isAfter(updatedAt.plusMinutes(2));
    }

    public boolean available(UnknownPlayer unknownPlayer, Clock clock) {
        if (!unknownPlayer.updateAvailable(clock)) {
            return false;
        }

        if (status.finished()) {
            return true;
        }

        return LocalDateTime.now(clock).isAfter(updatedAt.plusMinutes(2));
    }
}

package com.imstargg.core.domain.player;

import com.imstargg.core.domain.BrawlStarsTag;
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
        return OffsetDateTime.now(clock).isAfter(updatedAt.plusMinutes(2));
    }
}

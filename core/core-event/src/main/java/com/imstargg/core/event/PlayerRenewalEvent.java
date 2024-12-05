package com.imstargg.core.event;

public record PlayerRenewalEvent(
        RenewalType type,
        String tag
) {
}

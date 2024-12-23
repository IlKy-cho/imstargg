package com.imstargg.core.event;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app.event.queue.player-renewal")
public record PlayerRenewalEventQueueProperties(
        String name
) {
}

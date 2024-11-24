package com.imstargg.client.brawlstars;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.client.brawlstars")
record BrawlStarsClientProperties(
        String key,
        String url,
        RateLimit rateLimit
) {

    record RateLimit(
            int limit,
            int timeWindow
    ) {
        RateLimit {
            if (limit <= 0) {
                throw new IllegalArgumentException("Rate limit must be greater than 0");
            }
            if (timeWindow <= 0) {
                throw new IllegalArgumentException("Time window must be greater than 0");
            }
        }
    }
}

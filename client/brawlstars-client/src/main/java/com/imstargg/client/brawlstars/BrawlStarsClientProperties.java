package com.imstargg.client.brawlstars;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.client.brawlstars")
record BrawlStarsClientProperties(
        String key,
        String url
) {
}

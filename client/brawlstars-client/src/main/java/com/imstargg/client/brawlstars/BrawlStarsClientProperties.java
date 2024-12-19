package com.imstargg.client.brawlstars;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app.client.brawlstars")
record BrawlStarsClientProperties(
        List<String> keys,
        String url
) {
}

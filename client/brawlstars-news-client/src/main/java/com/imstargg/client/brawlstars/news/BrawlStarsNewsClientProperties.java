package com.imstargg.client.brawlstars.news;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.client.brawlstars-news")
record BrawlStarsNewsClientProperties(
        String url
) {
}

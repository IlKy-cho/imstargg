package com.imstargg.admin.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app.image.brawlstars")
public record BrawlStarsImageProperties(
        String bucketName
) {
}

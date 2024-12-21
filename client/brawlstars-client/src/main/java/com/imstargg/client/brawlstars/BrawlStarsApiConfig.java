package com.imstargg.client.brawlstars;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(BrawlStarsClientProperties.class)
class BrawlStarsApiConfig {

    private final BrawlStarsClientProperties brawlStarsClientProperties;

    BrawlStarsApiConfig(BrawlStarsClientProperties brawlStarsClientProperties) {
        this.brawlStarsClientProperties = brawlStarsClientProperties;
    }

    @Bean
    public BrawlStarsApiKeyInterceptor brawlStarsApiKeyInterceptor() {
        return new BrawlStarsApiKeyInterceptor(brawlStarsClientProperties.keys());
    }

}

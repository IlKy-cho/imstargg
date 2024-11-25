package com.imstargg.client.brawlstars;

import feign.Retryer;
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
    public AuthorizationKeyInterceptor brawlStarsApiKeyInterceptor() {
        return new AuthorizationKeyInterceptor(brawlStarsClientProperties.key());
    }

    @Bean
    public Retryer.Default brawlStarsApiRetryer() {
        return new Retryer.Default();
    }

    @Bean
    public BrawlStarsApiErrorDecoder brawlStarsApiErrorDecoder() {
        return new BrawlStarsApiErrorDecoder();
    }
}

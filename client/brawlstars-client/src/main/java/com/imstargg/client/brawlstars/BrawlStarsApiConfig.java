package com.imstargg.client.brawlstars;

import feign.Retryer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.concurrent.TimeUnit.SECONDS;

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

    @Bean
    public Retryer.Default brawlStarsApiRetryer() {
        return new Retryer.Default(1000, SECONDS.toMillis(10), 5);
    }

    @Bean
    public BrawlStarsApiErrorDecoder brawlStarsApiErrorDecoder() {
        return new BrawlStarsApiErrorDecoder();
    }
}

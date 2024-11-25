package com.imstargg.client.brawlstars;

import com.imstargg.support.ratelimit.RateLimiter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@EnableFeignClients
@Configuration
@EnableConfigurationProperties(BrawlStarsClientProperties.class)
class BrawlStarsClientConfig {

    private final BrawlStarsClientProperties brawlStarsClientProperties;

    public BrawlStarsClientConfig(BrawlStarsClientProperties brawlStarsClientProperties) {
        this.brawlStarsClientProperties = brawlStarsClientProperties;
    }

    @Bean
    public BrawlStarsClient brawlStarsClient(
            BrawlStarsApi brawlStarsApi,
            Clock clock
    ) {
        return new BrawlStarsClient(brawlStarsApi, createBrawlStarsClientRateLimiter(clock));
    }

    private RateLimiter createBrawlStarsClientRateLimiter(Clock clock) {
        return new RateLimiter(
                clock,
                brawlStarsClientProperties.rateLimit().limit(),
                brawlStarsClientProperties.rateLimit().timeWindow(),
                BrawlStarsClient.class.getName()
        );
    }
}

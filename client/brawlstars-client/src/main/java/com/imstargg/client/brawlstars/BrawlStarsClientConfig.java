package com.imstargg.client.brawlstars;

import com.imstargg.support.ratelimit.RateLimiter;
import feign.Retryer;
import org.springframework.beans.factory.annotation.Value;
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

    @Bean
    public AuthorizationKeyInterceptor authorizationKeyInterceptor(
            @Value("${app.client.brawlstars.key}") String key
    ) {
        return new AuthorizationKeyInterceptor(key);
    }

    @Bean
    public Retryer.Default retryer() {
        return new Retryer.Default();
    }
}

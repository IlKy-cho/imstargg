package com.imstargg.client.brawlstars;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
            BrawlStarsApi brawlStarsApi
    ) {
        return new BrawlStarsClient(brawlStarsApi);
    }
}

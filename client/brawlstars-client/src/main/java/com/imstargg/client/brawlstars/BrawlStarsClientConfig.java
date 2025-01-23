package com.imstargg.client.brawlstars;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class BrawlStarsClientConfig {

    @Bean
    public BrawlStarsClient brawlStarsClient(
            BrawlStarsApi brawlStarsApi
    ) {
        return new BrawlStarsClient(brawlStarsApi);
    }
}

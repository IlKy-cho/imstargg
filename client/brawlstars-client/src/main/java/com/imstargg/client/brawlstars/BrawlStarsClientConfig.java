package com.imstargg.client.brawlstars;

import com.imstargg.client.core.DefaultClientErrorDecoder;
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

    @Bean
    public BrawlStarsClientErrorDecoder brawlStarsClientErrorDecoder(DefaultClientErrorDecoder defaultClientErrorDecoder) {
        return new BrawlStarsClientErrorDecoder(defaultClientErrorDecoder);
    }
}

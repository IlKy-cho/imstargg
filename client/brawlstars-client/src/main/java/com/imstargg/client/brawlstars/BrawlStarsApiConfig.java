package com.imstargg.client.brawlstars;

import com.imstargg.client.core.DefaultClientErrorDecoder;
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

    @Bean
    public BrawlStarsClientErrorDecoder brawlStarsClientErrorDecoder(DefaultClientErrorDecoder defaultClientErrorDecoder) {
        return new BrawlStarsClientErrorDecoder(defaultClientErrorDecoder);
    }

}

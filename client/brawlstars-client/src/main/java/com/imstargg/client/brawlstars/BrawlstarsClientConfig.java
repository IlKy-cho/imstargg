package com.imstargg.client.brawlstars;

import feign.Retryer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients
@Configuration
class BrawlstarsClientConfig {

    @Bean
    public AuthorizationKeyInterceptor authorizationKeyInterceptor(
            @Value("${app.client.brawlstars.key}") String key
    ) {
        return new AuthorizationKeyInterceptor(key);
    }

    @Bean
    Retryer.Default retryer() {
        return new Retryer.Default();
    }
}

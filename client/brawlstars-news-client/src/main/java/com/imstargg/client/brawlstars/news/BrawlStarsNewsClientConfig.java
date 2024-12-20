package com.imstargg.client.brawlstars.news;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(BrawlStarsNewsClientProperties.class)
@EnableFeignClients
class BrawlStarsNewsClientConfig {

    private final BrawlStarsNewsClientProperties brawlStarsNewsClientProperties;

    public BrawlStarsNewsClientConfig(BrawlStarsNewsClientProperties brawlStarsNewsClientProperties) {
        this.brawlStarsNewsClientProperties = brawlStarsNewsClientProperties;
    }

    @Bean
    public BrawlStarsNewsClient brawlStarsNewsClient(BrawlStarsNewsHtmlApi brawlStarsNewsHtmlApi) {
        ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new JavaTimeModule());
        
        return new BrawlStarsNewsClient(
                brawlStarsNewsClientProperties.url(),
                objectMapper,
                brawlStarsNewsHtmlApi
        );
    }
}

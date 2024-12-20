package com.imstargg.client.brawlstars.news;

import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.concurrent.TimeUnit.SECONDS;

@Configuration
class BrawlStarsNewsHtmlApiConfig {

    @Bean
    public Retryer.Default brawlStarsNewsHtmlApiRetryer() {
        return new Retryer.Default(1000, SECONDS.toMillis(10), 5);
    }

    @Bean
    public BrawlStarsNewsHtmlApiErrorDecoder brawlStarsNewsHtmlApiErrorDecoder() {
        return new BrawlStarsNewsHtmlApiErrorDecoder();
    }
}

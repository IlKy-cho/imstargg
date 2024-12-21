package com.imstargg.client.core;

import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.concurrent.TimeUnit.SECONDS;

@Configuration
public class DefaultClientRetryConfig {

    @Bean
    public Retryer.Default defaultClientRetry() {
        return new Retryer.Default(1000, SECONDS.toMillis(10), 5);
    }

    @Bean
    public DefaultClientErrorDecoder defaultClientErrorDecoder() {
        return new DefaultClientErrorDecoder();
    }
}

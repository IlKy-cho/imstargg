package com.imstargg.client.core;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DefaultClientErrorDecoderConfig {

    @Bean
    public DefaultClientErrorDecoder defaultClientErrorDecoder() {
        return new DefaultClientErrorDecoder();
    }
}

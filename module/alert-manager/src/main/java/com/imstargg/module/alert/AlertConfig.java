package com.imstargg.module.alert;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties({AlertProperties.class, DiscordAlertProperties.class})
class AlertConfig {

    @Bean
    @ConditionalOnProperty(prefix = "app.alert", name = "type", havingValue = "NO_OPS", matchIfMissing = true)
    public NoOpsAlertManager noOpsAlertManager() {
        return new NoOpsAlertManager();
    }

    @Bean
    @ConditionalOnProperty(prefix = "app.alert", name = "type", havingValue = "DISCORD")
    public DiscordAlertManager discordAlertManager(
            DiscordAlertProperties discordAlertProperties, RestClient.Builder restClientBuilder
    ) {
        return new DiscordAlertManager(restClientBuilder, discordAlertProperties.webhookUrl());
    }
}

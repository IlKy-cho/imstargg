package com.imstargg.support.alert;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.alert.discord")
record DiscordAlertProperties(
        String webhookUrl
) {
}

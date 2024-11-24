package com.imstargg.support.alert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClient;

class DiscordAlertManager implements AlertManager {

    private static final Logger log = LoggerFactory.getLogger(DiscordAlertManager.class);

    private final RestClient restClient;
    private final String webhookUrl;

    public DiscordAlertManager(RestClient.Builder restClientBuilder, String webhookUrl) {
        this.restClient = restClientBuilder.build();
        this.webhookUrl = webhookUrl;
    }

    @Override
    public void alert(AlertCommand alertCommand) {
        try {
            restClient.post()
                    .uri(webhookUrl)
                    .body(DiscordWebhookRequest.from(alertCommand))
                    .retrieve();
        } catch (Exception ex) {
            log.error("Discord 알람 전송 실패", ex);
        }
    }
}

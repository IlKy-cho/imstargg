package com.imstargg.core.domain;

import com.imstargg.core.event.PlayerRenewalEvent;
import com.imstargg.core.event.PlayerRenewalEventQueueProperties;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties(PlayerRenewalEventQueueProperties.class)
public class PlayerRenewEventPublisher {

    private final SqsTemplate sqsTemplate;
    private final PlayerRenewalEventQueueProperties queueProperties;

    public PlayerRenewEventPublisher(
            SqsTemplate sqsTemplate,
            PlayerRenewalEventQueueProperties queueProperties
    ) {
        this.sqsTemplate = sqsTemplate;
        this.queueProperties = queueProperties;
    }

    public void publish(BrawlStarsTag tag) {
        sqsTemplate.send(to -> to
                .queue(queueProperties.name())
                .payload(new PlayerRenewalEvent(tag.value()))
        );
    }
}

package com.imstargg.core.domain;

import com.imstargg.core.event.PlayerRenewalEvent;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.stereotype.Component;

@Component
public class PlayerRenewEventPublisher {

    private final SqsTemplate sqsTemplate;

    public PlayerRenewEventPublisher(SqsTemplate sqsTemplate) {
        this.sqsTemplate = sqsTemplate;
    }

    public void publish(BrawlStarsTag tag) {
        sqsTemplate.send(new PlayerRenewalEvent(tag.value()));
    }
}

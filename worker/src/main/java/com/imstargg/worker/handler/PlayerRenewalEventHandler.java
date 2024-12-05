package com.imstargg.worker.handler;

import com.imstargg.core.event.PlayerRenewalEvent;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
class PlayerRenewalEventHandler {

    @SqsListener(
            queueNames = "${app.event.player-renewal-queue-name}",
            pollTimeoutSeconds = "10"
    )
    void handlePlayerRenewalEvent(PlayerRenewalEvent event) {

    }
}

package com.imstargg.worker.handler;

import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class PlayerRenewalEventHandler {

    private static final Logger log = LoggerFactory.getLogger(PlayerRenewalEventHandler.class);

    @SqsListener(
            queueNames = "${app.event.queue.player-renewal}",
            pollTimeoutSeconds = "10"
    )
    void handlePlayerRenewalEvent(String test) {
        log.info("message={}", test);
    }
}

package com.imstargg.worker.handler;

import com.imstargg.core.event.PlayerRenewalEvent;
import com.imstargg.core.event.RenewalType;
import com.imstargg.worker.domain.PlayerRenewer;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class PlayerRenewalEventHandler {

    private static final Logger log = LoggerFactory.getLogger(PlayerRenewalEventHandler.class);

    private final PlayerRenewer playerRenewer;

    public PlayerRenewalEventHandler(PlayerRenewer playerRenewer) {
        this.playerRenewer = playerRenewer;
    }

    @SqsListener(
            queueNames = "${app.event.queue.player-renewal}",
            pollTimeoutSeconds = "10"
    )
    void handlePlayerRenewalEvent(PlayerRenewalEvent event) {
        log.debug("플레이어 갱신 이벤트 수신 event={}", event);
        if (event.type() == RenewalType.RENEW) {
            playerRenewer.renew(event.tag());
        }
        else {
            throw new IllegalArgumentException("지원하지 않는 갱신 타입입니다. type=" + event.type());
        }
    }
}

package com.imstargg.worker.handler;

import com.imstargg.core.event.PlayerRenewalEvent;
import com.imstargg.support.alert.AlertCommand;
import com.imstargg.support.alert.AlertManager;
import com.imstargg.worker.domain.PlayerRenewer;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class PlayerRenewalEventHandler {

    private static final Logger log = LoggerFactory.getLogger(PlayerRenewalEventHandler.class);

    private final AlertManager alertManager;
    private final PlayerRenewer playerRenewer;

    public PlayerRenewalEventHandler(AlertManager alertManager, PlayerRenewer playerRenewer) {
        this.alertManager = alertManager;
        this.playerRenewer = playerRenewer;
    }

    @SqsListener(
            queueNames = "${app.event.queue.player-renewal}",
            pollTimeoutSeconds = "10"
    )
    void handlePlayerRenewalEvent(PlayerRenewalEvent event) {
        log.debug("플레이어 갱신 이벤트 수신 event={}", event);
        try {
            playerRenewer.renew(event.tag());
        } catch (Exception ex) {
            log.error("플레이어 갱신 중 예외 발생. tag={}", event.tag(), ex);
            alertManager.alert(AlertCommand.builder()
                    .error()
                    .title("플레이어 갱신 중 예외 발생")
                    .content("- tag=" + event.tag())
                    .ex(ex)
                    .build());
        }
    }

}

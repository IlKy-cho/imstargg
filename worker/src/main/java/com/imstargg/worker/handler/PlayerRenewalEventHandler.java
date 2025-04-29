package com.imstargg.worker.handler;

import com.imstargg.core.event.PlayerRenewalEvent;
import com.imstargg.support.alert.AlertCommand;
import com.imstargg.support.alert.AlertManager;
import com.imstargg.worker.domain.PlayerRenewalService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class PlayerRenewalEventHandler {

    private static final Logger log = LoggerFactory.getLogger(PlayerRenewalEventHandler.class);

    private final AlertManager alertManager;
    private final PlayerRenewalService playerRenewalService;

    public PlayerRenewalEventHandler(AlertManager alertManager, PlayerRenewalService playerRenewalService) {
        this.alertManager = alertManager;
        this.playerRenewalService = playerRenewalService;
    }

    @SqsListener(
            queueNames = "${app.event.queue.player-renewal.name}",
            acknowledgementMode = "MANUAL",
            messageVisibilitySeconds = "10"
    )
    void handlePlayerRenewalEvent(PlayerRenewalEvent event, Acknowledgement acknowledgement) {
        log.info("플레이어 갱신 이벤트 시작 tag={}", event.tag());
        try {
            playerRenewalService.renew(event.tag());
            log.info("플레이어 갱신 완료 tag={}", event.tag());
        } catch (Exception ex) {
            log.error("플레이어 갱신 중 예외 발생. tag={}", event.tag(), ex);
            alertManager.alert(AlertCommand.builder()
                    .error()
                    .title("플레이어 갱신 중 예외 발생")
                    .content("- tag=" + event.tag())
                    .ex(ex)
                    .build());
        }
        acknowledgement.acknowledgeAsync();
    }

}

package com.imstargg.worker.handler;

import com.imstargg.core.event.PlayerRenewalEvent;
import com.imstargg.support.alert.AlertCommand;
import com.imstargg.support.alert.AlertManager;
import com.imstargg.worker.domain.PlayerRenewalService;
import com.imstargg.worker.error.WorkerException;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class PlayerRenewalEventHandler {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(PlayerRenewalEventHandler.class);

    private final AlertManager alertManager;
    private final PlayerRenewalService playerRenewalService;
    private final Logger logger;

    public PlayerRenewalEventHandler(
            AlertManager alertManager,
            PlayerRenewalService playerRenewalService,
            Logger logger
    ) {
        this.alertManager = alertManager;
        this.playerRenewalService = playerRenewalService;
        this.logger = logger;
    }

    @SqsListener(
            queueNames = "${app.event.queue.player-renewal.name}",
            acknowledgementMode = "MANUAL",
            messageVisibilitySeconds = "10"
    )
    void handlePlayerRenewalEvent(PlayerRenewalEvent event, Acknowledgement acknowledgement) {
        logger.logRenewalStart(event);
        try {
            playerRenewalService.renew(event.tag());
            logger.logRenewalComplete(event);
        }
        catch (WorkerException ex) {
            logger.logWorkerException(ex);
        }
        catch (Exception ex) {
            logger.logException(ex);
            alertManager.alert(AlertCommand.builder()
                    .error()
                    .title("플레이어 갱신 중 예외 발생")
                    .content("- tag=" + event.tag())
                    .ex(ex)
                    .build());
        } finally {
            acknowledgement.acknowledgeAsync();
        }
    }

    @Component
    static class Logger {

        public void logRenewalStart(PlayerRenewalEvent event) {
            log.info("플레이어 갱신 시작 tag={}", event.tag());
        }

        public void logRenewalComplete(PlayerRenewalEvent event) {
            log.info("플레이어 갱신 완료 tag={}", event.tag());
        }

        public void logWorkerException(Exception ex) {
            log.warn("{} : {}", ex.getClass().getSimpleName(), ex.getMessage(), ex);
        }

        public void logException(Exception ex) {
            log.error("{} : {}", ex.getClass().getSimpleName(), ex.getMessage(), ex);
        }
    }
}

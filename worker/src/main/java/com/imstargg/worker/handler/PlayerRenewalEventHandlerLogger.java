package com.imstargg.worker.handler;

import com.imstargg.core.event.PlayerRenewalEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PlayerRenewalEventHandlerLogger {

    private static final Logger log = LoggerFactory.getLogger(PlayerRenewalEventHandlerLogger.class);

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

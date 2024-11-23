package com.imstargg.module.alert;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class NoOpsAlertManager implements AlertManager {

    private static final Logger log = LoggerFactory.getLogger(NoOpsAlertManager.class);

    @Override
    public void alert(AlertCommand alertCommand) {
        log.info("[알람 호출]:\n{}", alertCommand);
    }
}

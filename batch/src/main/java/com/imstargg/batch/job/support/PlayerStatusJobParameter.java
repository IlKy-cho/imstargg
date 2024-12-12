package com.imstargg.batch.job.support;

import com.imstargg.core.enums.PlayerStatus;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class PlayerStatusJobParameter {

    private static final Logger log = LoggerFactory.getLogger(PlayerStatusJobParameter.class);

    @Nullable
    private PlayerStatus status;

    @PostConstruct
    void init() {
        log.debug("JobParameter[player.status]={}", status);
    }

    @Value("#{jobParameters['player.status']}")
    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    @Nullable
    public PlayerStatus getStatus() {
        return status;
    }
}

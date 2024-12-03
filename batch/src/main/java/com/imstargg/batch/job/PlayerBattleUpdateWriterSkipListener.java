package com.imstargg.batch.job;

import com.imstargg.batch.domain.PlayerBattleUpdateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.SkipListener;

public class PlayerBattleUpdateWriterSkipListener implements SkipListener<Object, PlayerBattleUpdateResult> {

    private static final Logger log = LoggerFactory.getLogger(PlayerBattleUpdateWriterSkipListener.class);

    @Override
    public void onSkipInWrite(PlayerBattleUpdateResult item, Throwable t) {
        log.warn("Player 업데이트 저장 중 예외 발생. playerTag={}", item.playerEntity().getBrawlStarsTag(), t);
    }
}

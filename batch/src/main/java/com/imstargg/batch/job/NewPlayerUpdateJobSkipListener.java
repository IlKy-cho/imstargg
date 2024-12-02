package com.imstargg.batch.job;

import com.imstargg.batch.domain.NewPlayer;
import com.imstargg.storage.db.core.UnknownPlayerCollectionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.SkipListener;

public class NewPlayerUpdateJobSkipListener implements SkipListener<UnknownPlayerCollectionEntity, NewPlayer> {

    private static final Logger log = LoggerFactory.getLogger(NewPlayerUpdateJobSkipListener.class);

    @Override
    public void onSkipInWrite(NewPlayer item, Throwable t) {
        log.warn("Player 업데이트 저장 중 예외 발생. playerTag={}", item.playerEntity().getBrawlStarsTag(), t);
    }
}

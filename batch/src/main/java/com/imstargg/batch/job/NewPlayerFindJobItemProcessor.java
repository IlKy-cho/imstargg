package com.imstargg.batch.job;


import com.imstargg.batch.domain.PlayerTagFinderWithLocalCache;
import com.imstargg.storage.db.core.BattlePlayerCollectionEntity;
import com.imstargg.storage.db.core.UnknownPlayerCollectionEntity;
import org.springframework.batch.item.ItemProcessor;

import java.time.Clock;

public class NewPlayerFindJobItemProcessor implements ItemProcessor<BattlePlayerCollectionEntity, UnknownPlayerCollectionEntity> {

    private final Clock clock;
    private final PlayerTagFinderWithLocalCache playerTagFinder;

    public NewPlayerFindJobItemProcessor(Clock clock, PlayerTagFinderWithLocalCache playerTagFinder) {
        this.clock = clock;
        this.playerTagFinder = playerTagFinder;
    }

    @Override
    public UnknownPlayerCollectionEntity process(BattlePlayerCollectionEntity item) throws Exception {
        if (playerTagFinder.exists(item.getBrawlStarsTag())) {
            return null;
        }

        return UnknownPlayerCollectionEntity.updateNew(item.getBrawlStarsTag(), clock);
    }
}

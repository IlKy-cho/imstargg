package com.imstargg.batch.job;


import com.imstargg.batch.domain.PlayerTagSet;
import com.imstargg.storage.db.core.BattlePlayerCollectionEntity;
import com.imstargg.storage.db.core.UnknownPlayerCollectionEntity;
import org.springframework.batch.item.ItemProcessor;

import java.time.Clock;

public class NewPlayerFindJobItemProcessor implements ItemProcessor<BattlePlayerCollectionEntity, UnknownPlayerCollectionEntity> {

    private final Clock clock;
    private final PlayerTagSet playerTagSet;

    public NewPlayerFindJobItemProcessor(Clock clock, PlayerTagSet playerTagSet) {
        this.clock = clock;
        this.playerTagSet = playerTagSet;
    }

    @Override
    public UnknownPlayerCollectionEntity process(BattlePlayerCollectionEntity item) throws Exception {
        if (playerTagSet.contains(item.getBrawlStarsTag())) {
            return null;
        }

        return UnknownPlayerCollectionEntity.updateNew(item.getBrawlStarsTag(), clock);
    }
}

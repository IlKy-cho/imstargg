package com.imstargg.batch.job;


import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattleCollectionEntityTeamPlayer;
import com.imstargg.storage.db.core.UnknownPlayerCollectionEntity;
import org.springframework.batch.item.ItemProcessor;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

public class NewPlayerFindJobItemProcessor
        implements ItemProcessor<BattleCollectionEntity, List<UnknownPlayerCollectionEntity>> {

    private final ConcurrentSkipListSet<String> tagCache = new ConcurrentSkipListSet<>();

    @Override
    public List<UnknownPlayerCollectionEntity> process(BattleCollectionEntity item) throws Exception {
        return item.getTeams().stream().flatMap(Collection::stream)
                .map(BattleCollectionEntityTeamPlayer::getBrawlStarsTag)
                .filter(tagCache::add)
                .map(UnknownPlayerCollectionEntity::updateNew)
                .toList();
    }
}

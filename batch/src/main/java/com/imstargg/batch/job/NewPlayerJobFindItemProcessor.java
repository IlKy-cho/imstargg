package com.imstargg.batch.job;

import com.imstargg.batch.domain.PlayerTagFilter;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattleCollectionEntityTeamPlayer;
import org.springframework.batch.item.ItemProcessor;

import java.util.List;

public class NewPlayerJobFindItemProcessor
        implements ItemProcessor<BattleCollectionEntity, List<String>> {

    private final PlayerTagFilter playerTagFilter;

    public NewPlayerJobFindItemProcessor(PlayerTagFilter playerTagFilter) {
        this.playerTagFilter = playerTagFilter;
    }

    @Override
    public List<String> process(BattleCollectionEntity item) throws Exception {
        return playerTagFilter.filter(
                item.getAllPlayers()
                        .stream()
                        .map(BattleCollectionEntityTeamPlayer::getBrawlStarsTag)
                        .toList()
        );
    }
}

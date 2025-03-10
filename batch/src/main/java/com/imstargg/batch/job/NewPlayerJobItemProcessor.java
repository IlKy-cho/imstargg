package com.imstargg.batch.job;

import com.imstargg.batch.domain.PlayerTagFilter;
import com.imstargg.client.brawlstars.BrawlStarsClientException;
import com.imstargg.collection.domain.PlayerUpdater;
import com.imstargg.collection.domain.PlayerUpdaterFactory;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattleCollectionEntityTeamPlayer;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.util.ArrayList;
import java.util.List;

public class NewPlayerJobItemProcessor
        implements ItemProcessor<BattleCollectionEntity, List<PlayerCollectionEntity>> {

    private static final Logger log = LoggerFactory.getLogger(NewPlayerJobItemProcessor.class);

    private final PlayerTagFilter playerTagFilter;
    private final PlayerUpdaterFactory playerUpdaterFactory;

    public NewPlayerJobItemProcessor(PlayerTagFilter playerTagFilter, PlayerUpdaterFactory playerUpdaterFactory) {
        this.playerTagFilter = playerTagFilter;
        this.playerUpdaterFactory = playerUpdaterFactory;
    }

    @Override
    public List<PlayerCollectionEntity> process(BattleCollectionEntity item) throws Exception {
        List<String> newPlayerTags = fineNewPlayerTags(item);
        List<PlayerCollectionEntity> newPlayers = new ArrayList<>();

        for (String newPlayerTag : newPlayerTags) {
            PlayerCollectionEntity playerEntity = new PlayerCollectionEntity(newPlayerTag);
            PlayerUpdater playerUpdater = playerUpdaterFactory.create(playerEntity);
            try {
                playerUpdater.updatePlayer();
                newPlayers.add(playerUpdater.getPlayerEntity());
            } catch (BrawlStarsClientException.NotFound ex) {
                log.info("새로운 Player 가 존재하지 않는 것으로 확인되어 스킵. playerTag={}", newPlayerTag);
            }
        }

        log.debug("{}명 Player 가 새로 추가되었습니다.", newPlayers.size());
        return newPlayers;
    }

    private List<String> fineNewPlayerTags(BattleCollectionEntity item) {
        return playerTagFilter.filter(
                item.getAllPlayers()
                        .stream()
                        .map(BattleCollectionEntityTeamPlayer::getBrawlStarsTag)
                        .toList()
        );
    }
}

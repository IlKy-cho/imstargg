package com.imstargg.batch.job;

import com.imstargg.batch.domain.PlayerTagFilter;
import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.client.brawlstars.BrawlStarsClientException;
import com.imstargg.client.brawlstars.response.PlayerResponse;
import com.imstargg.collection.domain.PlayerUpdateApplier;
import com.imstargg.storage.db.core.player.BattleCollectionEntity;
import com.imstargg.storage.db.core.player.BattleCollectionEntityTeamPlayer;
import com.imstargg.storage.db.core.player.PlayerCollectionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.util.ArrayList;
import java.util.List;

public class NewPlayerJobItemProcessor
        implements ItemProcessor<BattleCollectionEntity, List<PlayerCollectionEntity>> {

    private static final Logger log = LoggerFactory.getLogger(NewPlayerJobItemProcessor.class);

    private final PlayerTagFilter playerTagFilter;
    private final BrawlStarsClient brawlStarsClient;
    private final PlayerUpdateApplier playerUpdateApplier;

    public NewPlayerJobItemProcessor(
            PlayerTagFilter playerTagFilter,
            BrawlStarsClient brawlStarsClient,
            PlayerUpdateApplier playerUpdateApplier
    ) {
        this.playerTagFilter = playerTagFilter;
        this.brawlStarsClient = brawlStarsClient;
        this.playerUpdateApplier = playerUpdateApplier;
    }

    @Override
    public List<PlayerCollectionEntity> process(BattleCollectionEntity item) throws Exception {
        List<String> newPlayerTags = fineNewPlayerTags(item);
        List<PlayerCollectionEntity> newPlayers = new ArrayList<>();

        for (String newPlayerTag : newPlayerTags) {
            try {
                PlayerResponse playerResponse = brawlStarsClient.getPlayerInformation(newPlayerTag);
                PlayerCollectionEntity playerEntity = playerUpdateApplier.create(playerResponse);
                newPlayers.add(playerEntity);
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

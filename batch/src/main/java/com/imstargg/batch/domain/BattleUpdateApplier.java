package com.imstargg.batch.domain;

import com.imstargg.client.brawlstars.response.BattleResponse;
import com.imstargg.client.brawlstars.response.BattleResultBrawlerResponse;
import com.imstargg.client.brawlstars.response.BattleResultPlayerResponse;
import com.imstargg.client.brawlstars.response.ListResponse;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattleCollectionEntityEvent;
import com.imstargg.storage.db.core.BattleCollectionEntityPlayer;
import com.imstargg.storage.db.core.BattlePlayerCollectionEntity;
import com.imstargg.storage.db.core.BattlePlayerCollectionEntityBrawler;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class BattleUpdateApplier {

    public List<BattleUpdateResult> update(
            PlayerCollectionEntity playerEntity,
            ListResponse<BattleResponse> battleListResponse,
            @Nullable BattleCollectionEntity lastBattleEntity
    ) {
        List<BattleResponse> battleResponseListToUpdate = getBattleResponseListToUpdate(battleListResponse, lastBattleEntity);

        List<BattleUpdateResult> results = new ArrayList<>();
        for (BattleResponse battleResponse : battleResponseListToUpdate) {
            BattleKeyBuilder battleKeyBuilder = new BattleKeyBuilder(battleResponse.battleTime());
            List<List<BattleResultPlayerResponse>> battleResponseTeamList = getBattleTeamList(battleResponse);
            battleResponseTeamList.stream()
                    .flatMap(Collection::stream)
                    .forEach(player -> battleKeyBuilder.addPlayerTag(player.tag()));
            BattleCollectionEntity battleEntity = new BattleCollectionEntity(
                    battleKeyBuilder.build(),
                    battleResponse.battleTime(),
                    new BattleCollectionEntityEvent(
                            battleResponse.event().id(),
                            battleResponse.event().mode(),
                            battleResponse.event().map()
                    ),
                    battleResponse.battle().mode(),
                    battleResponse.battle().type(),
                    battleResponse.battle().result(),
                    battleResponse.battle().duration(),
                    Optional.ofNullable(battleResponse.battle().starPlayer())
                            .map(BattleResultPlayerResponse::tag).orElse(null),
                    new BattleCollectionEntityPlayer(
                            playerEntity.getId(),
                            battleResponse.battle().rank(),
                            battleResponse.battle().trophyChange(),
                            playerEntity.getTrophies()
                    )
            );

            List<BattlePlayerCollectionEntity> battlePlayerEntities = createBattlePlayers(
                    battleEntity, battleResponseTeamList);

            results.add(new BattleUpdateResult(battleEntity, battlePlayerEntities));
        }

        return results;
    }

    private List<BattleResponse> getBattleResponseListToUpdate(
            ListResponse<BattleResponse> battleListResponse, @Nullable BattleCollectionEntity lastBattleEntity) {
        return Optional.ofNullable(lastBattleEntity)
                .map(battle -> battleListResponse.items().stream()
                        .filter(battleResponse -> battleResponse.battleTime().isAfter(battle.getBattleTime()))
                        .toList())
                .orElse(battleListResponse.items());
    }

    private List<List<BattleResultPlayerResponse>> getBattleTeamList(BattleResponse battleResponse) {
        return Optional.ofNullable(battleResponse.battle().teams())
                .or(() -> Optional.ofNullable(battleResponse.battle().players())
                        .map(players -> players.stream().map(List::of).toList())
                ).orElseThrow(() -> new IllegalArgumentException("플레이어 정보가 없습니다. " +
                        "battleResponse = " + battleResponse));
    }

    private List<BattlePlayerCollectionEntity> createBattlePlayers(
            BattleCollectionEntity battleEntity, List<List<BattleResultPlayerResponse>> teams) {
        List<BattlePlayerCollectionEntity> results = new ArrayList<>();
        for (int teamIdx = 0; teamIdx < teams.size(); teamIdx++) {
            for (int playerIdx = 0; playerIdx < teams.get(teamIdx).size(); playerIdx++) {
                BattleResultPlayerResponse playerResponse = teams.get(teamIdx).get(playerIdx);
                List<BattleResultBrawlerResponse> brawlerResponseList = Optional.ofNullable(playerResponse.brawler())
                        .map(List::of)
                        .or(() -> Optional.ofNullable(playerResponse.brawlers()))
                        .orElseThrow(() -> new IllegalArgumentException("브롤러가 없습니다. " +
                                "playerResponse=" + playerResponse));

                for (BattleResultBrawlerResponse brawlerResponse : brawlerResponseList) {
                    results.add(new BattlePlayerCollectionEntity(
                            battleEntity,
                            playerResponse.tag(),
                            playerResponse.name(),
                            teamIdx,
                            playerIdx,
                            new BattlePlayerCollectionEntityBrawler(
                                    brawlerResponse.id(),
                                    brawlerResponse.name(),
                                    brawlerResponse.power(),
                                    brawlerResponse.trophies(),
                                    brawlerResponse.trophyChange()
                            )
                    ));
                }
            }
        }

        return results;
    }
}

package com.imstargg.batch.domain;

import com.imstargg.client.brawlstars.response.BattleResponse;
import com.imstargg.client.brawlstars.response.BattleResultBrawlerResponse;
import com.imstargg.client.brawlstars.response.BattleResultPlayerResponse;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattleCollectionEntityEvent;
import com.imstargg.storage.db.core.BattleCollectionEntityPlayer;
import com.imstargg.storage.db.core.BattlePlayerCollectionEntityBrawler;
import com.imstargg.storage.db.core.PlayerCollectionEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class BattleCollectionEntityBuilder {

    private final PlayerCollectionEntity playerEntity;
    private final BattleResponse battleResponse;

    public BattleCollectionEntityBuilder(PlayerCollectionEntity playerEntity, BattleResponse battleResponse) {
        this.playerEntity = playerEntity;
        this.battleResponse = battleResponse;
    }

    public BattleCollectionEntity build() {
        List<List<BattleResultPlayerResponse>> battleResponseTeamList = getBattleTeamList();
        String battleKey = createBattleKey(battleResponseTeamList);
        BattleCollectionEntity battleEntity = buildBattleEntity(battleKey);
        addBattlePlayers(battleEntity, battleResponseTeamList);
        return battleEntity;
    }

    private List<List<BattleResultPlayerResponse>> getBattleTeamList() {
        return Optional.ofNullable(battleResponse.battle().teams())
                .or(() -> Optional.ofNullable(battleResponse.battle().players())
                        .map(players -> players.stream().map(List::of).toList())
                ).orElseThrow(() -> new IllegalArgumentException("플레이어 정보가 없습니다. " +
                        "battleResponse = " + battleResponse));
    }

    private String createBattleKey(List<List<BattleResultPlayerResponse>> battleResponseTeamList) {
        BattleKeyBuilder battleKeyBuilder = new BattleKeyBuilder(battleResponse.battleTime());
        battleResponseTeamList.stream()
                .flatMap(Collection::stream)
                .forEach(player -> battleKeyBuilder.addPlayerTag(player.tag()));
        return battleKeyBuilder.build();
    }

    private BattleCollectionEntity buildBattleEntity(String battleKey) {
        return new BattleCollectionEntity(
                battleKey,
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
                        playerEntity,
                        battleResponse.battle().rank(),
                        battleResponse.battle().trophyChange(),
                        playerEntity.getTrophies()
                )
        );
    }

    private void addBattlePlayers(
            BattleCollectionEntity battleEntity, List<List<BattleResultPlayerResponse>> teams) {
        for (int teamIdx = 0; teamIdx < teams.size(); teamIdx++) {
            for (int playerIdx = 0; playerIdx < teams.get(teamIdx).size(); playerIdx++) {
                BattleResultPlayerResponse playerResponse = teams.get(teamIdx).get(playerIdx);
                List<BattleResultBrawlerResponse> brawlerResponseList = Optional.ofNullable(playerResponse.brawler())
                        .map(List::of)
                        .or(() -> Optional.ofNullable(playerResponse.brawlers()))
                        .orElseThrow(() -> new IllegalArgumentException("브롤러가 없습니다. " +
                                "playerResponse=" + playerResponse));

                for (BattleResultBrawlerResponse brawlerResponse : brawlerResponseList) {
                    battleEntity.addPlayer(
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
                    );
                }
            }
        }
    }
}

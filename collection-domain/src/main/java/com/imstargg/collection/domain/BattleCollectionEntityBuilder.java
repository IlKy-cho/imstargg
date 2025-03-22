package com.imstargg.collection.domain;

import com.imstargg.client.brawlstars.response.BattleResponse;
import com.imstargg.client.brawlstars.response.BattleResultBrawlerResponse;
import com.imstargg.client.brawlstars.response.BattleResultPlayerResponse;
import com.imstargg.storage.db.core.player.BattleCollectionEntity;
import com.imstargg.storage.db.core.player.BattleCollectionEntityEvent;
import com.imstargg.storage.db.core.player.BattleCollectionEntityPlayer;
import com.imstargg.storage.db.core.player.BattleCollectionEntityTeamPlayer;
import com.imstargg.storage.db.core.player.BattleCollectionEntityTeamPlayerBrawler;
import com.imstargg.storage.db.core.player.PlayerCollectionEntity;

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
        List<List<BattleCollectionEntityTeamPlayer>> battleTeamPlayerEntities = buildBattleTeamPlayers(
                battleResponseTeamList);
        return buildBattleEntity(battleKey, battleTeamPlayerEntities);
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

    private BattleCollectionEntity buildBattleEntity(
            String battleKey, List<List<BattleCollectionEntityTeamPlayer>> battleTeamPlayerEntities) {
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
                        battleResponse.battle().trophyChange()
                ),
                battleTeamPlayerEntities
        );
    }

    private List<List<BattleCollectionEntityTeamPlayer>> buildBattleTeamPlayers(
            List<List<BattleResultPlayerResponse>> teams) {
        return teams.stream().map(
                team -> team.stream().flatMap(
                        player -> getPlayerBrawlers(player).stream().map(
                                brawler -> new BattleCollectionEntityTeamPlayer(
                                        player.tag(),
                                        player.name(),
                                        new BattleCollectionEntityTeamPlayerBrawler(
                                                brawler.id(),
                                                brawler.name(),
                                                brawler.power(),
                                                brawler.trophies(),
                                                brawler.trophyChange()
                                        )
                                ))
                ).toList()
        ).toList();
    }

    private static List<BattleResultBrawlerResponse> getPlayerBrawlers(BattleResultPlayerResponse player) {
        return Optional.ofNullable(player.brawler())
                .map(List::of)
                .or(() -> Optional.ofNullable(player.brawlers()))
                .orElseThrow(() -> new IllegalArgumentException("브롤러가 없습니다. " +
                        "player=" + player));
    }
}

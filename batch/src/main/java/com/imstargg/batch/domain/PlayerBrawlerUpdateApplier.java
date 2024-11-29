package com.imstargg.batch.domain;

import com.imstargg.client.brawlstars.response.AccessoryResponse;
import com.imstargg.client.brawlstars.response.BrawlerStatResponse;
import com.imstargg.client.brawlstars.response.GearStatResponse;
import com.imstargg.client.brawlstars.response.StarPowerResponse;
import com.imstargg.storage.db.core.PlayerBrawlerCollectionEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PlayerBrawlerUpdateApplier {

    public List<PlayerBrawlerCollectionEntity> update(
            long playerId,
            List<PlayerBrawlerCollectionEntity> playerBrawlerEntities,
            List<BrawlerStatResponse> brawlerResponseList
    ) {
        Map<Long, PlayerBrawlerCollectionEntity> idToBrawler = playerBrawlerEntities.stream()
                .collect(Collectors.toMap(PlayerBrawlerCollectionEntity::getBrawlerBrawlStarsId, Function.identity()));

        for (BrawlerStatResponse brawlerResponse : brawlerResponseList) {
            PlayerBrawlerCollectionEntity brawler = idToBrawler.computeIfAbsent(brawlerResponse.id(), id ->
                    new PlayerBrawlerCollectionEntity(
                            playerId,
                            brawlerResponse.id(),
                            brawlerResponse.power(),
                            brawlerResponse.rank(),
                            brawlerResponse.trophies(),
                            brawlerResponse.highestTrophies(),
                            brawlerResponse.gears().stream().map(GearStatResponse::id).toList(),
                            brawlerResponse.starPowers().stream().map(StarPowerResponse::id).toList(),
                            brawlerResponse.gadgets().stream().map(AccessoryResponse::id).toList()
                    ));

            brawler.setPower(brawlerResponse.power());
            brawler.setRank(brawlerResponse.rank());
            brawler.setTrophies(brawlerResponse.trophies());
            brawler.setHighestTrophies(brawlerResponse.highestTrophies());
            brawler.setGearBrawlStarsIds(
                    brawlerResponse.gears().stream().map(GearStatResponse::id).toList());
            brawler.setStarPowerBrawlStarsIds(
                    brawlerResponse.starPowers().stream().map(StarPowerResponse::id).toList());
            brawler.setGadgetBrawlStarsIds(
                    brawlerResponse.gadgets().stream().map(AccessoryResponse::id).toList());
        }

        return idToBrawler.values().stream().toList();
    }
}

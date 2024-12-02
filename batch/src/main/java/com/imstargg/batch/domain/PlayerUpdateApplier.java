package com.imstargg.batch.domain;

import com.imstargg.client.brawlstars.response.PlayerResponse;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

@Component
public class PlayerUpdateApplier {

    private final Clock clock;

    public PlayerUpdateApplier(Clock clock) {
        this.clock = clock;
    }

    public PlayerCollectionEntity create(PlayerResponse playerResponse) {
        return new PlayerCollectionEntity(
                playerResponse.tag(),
                playerResponse.name(),
                playerResponse.nameColor(),
                playerResponse.icon().id(),
                playerResponse.trophies(),
                playerResponse.highestTrophies(),
                playerResponse.expLevel(),
                playerResponse.expPoints(),
                playerResponse.isQualifiedFromChampionshipChallenge(),
                playerResponse.victories3vs3(),
                playerResponse.soloVictories(),
                playerResponse.duoVictories(),
                playerResponse.bestRoboRumbleTime(),
                playerResponse.bestTimeAsBigBrawler(),
                playerResponse.club().tag(),
                LocalDateTime.now(clock)
        );
    }

    public PlayerCollectionEntity update(
            PlayerCollectionEntity playerEntity, PlayerResponse playerResponse) {
        playerEntity.setName(playerResponse.name());
        playerEntity.setNameColor(playerResponse.nameColor());
        playerEntity.setIconBrawlStarsId(playerResponse.icon().id());
        playerEntity.setTrophies(playerResponse.trophies());
        playerEntity.setHighestTrophies(playerResponse.highestTrophies());
        playerEntity.setExpLevel(playerResponse.expLevel());
        playerEntity.setExpPoints(playerResponse.expPoints());
        playerEntity.setQualifiedFromChampionshipChallenge(playerResponse.isQualifiedFromChampionshipChallenge());
        playerEntity.setVictories3vs3(playerResponse.victories3vs3());
        playerEntity.setSoloVictories(playerResponse.soloVictories());
        playerEntity.setDuoVictories(playerResponse.duoVictories());
        playerEntity.setBestRoboRumbleTime(playerResponse.bestRoboRumbleTime());
        playerEntity.setBestTimeAsBigBrawler(playerResponse.bestTimeAsBigBrawler());
        playerEntity.setBrawlStarsClubTag(playerResponse.club().tag());
        return playerEntity;
    }
}

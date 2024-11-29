package com.imstargg.batch.domain;

import com.imstargg.client.brawlstars.response.PlayerResponse;
import com.imstargg.core.enums.PlayerStatus;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import org.springframework.stereotype.Component;

@Component
public class PlayerUpdater {

    public PlayerCollectionEntity update(
            PlayerCollectionEntity playerEntity, PlayerResponse playerResponse) {
        playerEntity.setStatus(PlayerStatus.UPDATED);
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

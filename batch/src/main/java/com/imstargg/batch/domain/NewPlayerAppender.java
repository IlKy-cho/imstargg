package com.imstargg.batch.domain;

import com.imstargg.client.brawlstars.response.PlayerResponse;
import com.imstargg.core.enums.PlayerStatus;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import com.imstargg.storage.db.core.PlayerCollectionJpaRepository;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

@Component
public class NewPlayerAppender {

    private final Clock clock;
    private final PlayerCollectionJpaRepository playerRepository;
    private final PlayerUpdateApplier playerUpdateApplier;

    public NewPlayerAppender(
            Clock clock,
            PlayerCollectionJpaRepository playerRepository,
            PlayerUpdateApplier playerUpdateApplier
    ) {
        this.clock = clock;
        this.playerRepository = playerRepository;
        this.playerUpdateApplier = playerUpdateApplier;
    }

    public PlayerCollectionEntity append(PlayerResponse playerResponse) {
        PlayerCollectionEntity player = new PlayerCollectionEntity(
                PlayerStatus.UPDATED,
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
        return playerRepository.save(player);
    }
}

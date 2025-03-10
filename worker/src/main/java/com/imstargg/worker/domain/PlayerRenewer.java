package com.imstargg.worker.domain;

import com.imstargg.client.brawlstars.BrawlStarsClientException;
import com.imstargg.collection.domain.PlayerUpdater;
import com.imstargg.collection.domain.PlayerUpdaterFactory;
import com.imstargg.core.enums.PlayerRenewalStatus;
import com.imstargg.core.enums.PlayerStatus;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import com.imstargg.storage.db.core.PlayerRenewalCollectionEntity;
import com.imstargg.storage.db.core.UnknownPlayerCollectionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PlayerRenewer {

    private static final Logger log = LoggerFactory.getLogger(PlayerRenewer.class);

    private final PlayerRepository playerRepository;
    private final PlayerRenewalRepository playerRenewalRepository;
    private final PlayerUpdaterFactory playerUpdaterFactory;

    public PlayerRenewer(
            PlayerRepository playerRepository,
            PlayerRenewalRepository playerRenewalRepository,
            PlayerUpdaterFactory playerUpdaterFactory
    ) {
        this.playerRepository = playerRepository;
        this.playerRenewalRepository = playerRenewalRepository;
        this.playerUpdaterFactory = playerUpdaterFactory;
    }

    public void renew(String tag) {
        PlayerRenewalCollectionEntity playerRenewalEntity = playerRenewalRepository.find(tag)
                .orElseThrow(() -> new IllegalStateException("플레이어 갱신 정보가 존재하지 않습니다. tag=" + tag));
        if (PlayerRenewalStatus.PENDING != playerRenewalEntity.getStatus()) {
            log.warn("플레이어 갱신 상태가 PENDING이 아니므로 갱신하지 않습니다. tag={}", tag);
            return;
        }

        try {
            playerRenewalRepository.executing(playerRenewalEntity);
            playerRepository.find(tag).ifPresentOrElse(
                    this::renewPlayer,
                    () -> playerRepository.findUnknown(tag).ifPresentOrElse(
                            this::renewNewPlayer,
                            () -> {
                                throw new IllegalStateException("플레이어 정보가 존재하지 않습니다. tag=" + tag);
                            }
                    )
            );

            playerRenewalRepository.complete(playerRenewalEntity);
        } catch (BrawlStarsClientException.InMaintenance e) {
            playerRenewalRepository.inMaintenance(playerRenewalEntity);
        } catch (Exception e) {
            playerRenewalRepository.failed(playerRenewalEntity);
            throw e;
        }
    }

    private void renewPlayer(PlayerCollectionEntity playerEntity) {
        log.debug("기존 플레이어 갱신 tag={}", playerEntity.getBrawlStarsTag());
        PlayerUpdater playerUpdater = playerUpdaterFactory.create(playerEntity);
        playerUpdater.update();
        playerRepository.update(playerUpdater.getPlayerEntity(), playerUpdater.getUpdatedBattleEntities());

    }

    public void renewNewPlayer(UnknownPlayerCollectionEntity unknownPlayerEntity) {
        log.debug("신규 플레이어 갱신 tag={}", unknownPlayerEntity.getBrawlStarsTag());
        PlayerCollectionEntity playerEntity = new PlayerCollectionEntity(unknownPlayerEntity.getBrawlStarsTag());
        PlayerUpdater playerUpdater = playerUpdaterFactory.create(playerEntity);
        playerUpdater.update();
        if (playerUpdater.getPlayerEntity().getStatus() == PlayerStatus.DELETED) {
            unknownPlayerEntity.notFound();
            playerRepository.update(unknownPlayerEntity);
        } else {
            playerRepository.update(
                    unknownPlayerEntity,
                    playerUpdater.getPlayerEntity(),
                    playerUpdater.getUpdatedBattleEntities()
            );
        }

    }
}

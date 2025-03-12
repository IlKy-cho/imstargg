package com.imstargg.worker.domain;

import com.imstargg.client.brawlstars.BrawlStarsClientException;
import com.imstargg.core.enums.PlayerRenewalStatus;
import com.imstargg.storage.db.core.PlayerRenewalCollectionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
public class PlayerRenewalService {

    private static final Logger log = LoggerFactory.getLogger(PlayerRenewalService.class);

    private final PlayerFinder playerFinder;
    private final PlayerRenewalUpdater playerRenewalUpdater;
    private final PlayerRenewalReader playerRenewalReader;
    private final PlayerRenewalProcessor playerRenewalProcessor;

    public PlayerRenewalService(
            PlayerFinder playerFinder,
            PlayerRenewalUpdater playerRenewalUpdater,
            PlayerRenewalReader playerRenewalReader,
            PlayerRenewalProcessor playerRenewalProcessor
    ) {
        this.playerFinder = playerFinder;
        this.playerRenewalUpdater = playerRenewalUpdater;
        this.playerRenewalReader = playerRenewalReader;
        this.playerRenewalProcessor = playerRenewalProcessor;
    }

    public void renew(String tag) {
        PlayerRenewalCollectionEntity playerRenewal = playerRenewalReader.get(tag);
        if (PlayerRenewalStatus.PENDING != playerRenewal.getStatus()) {
            log.warn("플레이어 갱신 상태가 PENDING이 아니므로 갱신하지 않습니다. tag={}", tag);
            return;
        }

        try {
            playerRenewal.executing();
            playerRenewalUpdater.update(playerRenewal);
            playerFinder.findPlayer(tag).ifPresentOrElse(
                    playerRenewalProcessor::renewPlayer,
                    () -> playerFinder.findUnknownPlayer(tag).ifPresentOrElse(
                            playerRenewalProcessor::renewNewPlayer,
                            () -> {
                                throw new IllegalStateException("플레이어 정보가 존재하지 않습니다. tag=" + tag);
                            }
                    )
            );

            playerRenewal.complete();
        } catch (BrawlStarsClientException.InMaintenance e) {
            playerRenewal.inMaintenance();
        } catch (OptimisticLockingFailureException e) {
            log.warn("플레이어가 이미 갱신 중입니다. tag={}", tag);
        } catch (Exception e) {
            playerRenewal.failed();
            throw e;
        } finally {
            playerRenewalUpdater.update(playerRenewal);
        }
    }

}

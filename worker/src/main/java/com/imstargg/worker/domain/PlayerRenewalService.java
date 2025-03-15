package com.imstargg.worker.domain;

import com.imstargg.client.brawlstars.BrawlStarsClientException;
import com.imstargg.core.enums.PlayerRenewalStatus;
import com.imstargg.storage.db.core.PlayerRenewalCollectionEntity;
import com.imstargg.worker.error.WorkerException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
public class PlayerRenewalService {

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
            throw new WorkerException("플레이어 갱신 상태가 PENDING이 아닙니다. tag=" + tag);
        }

        try {
            playerRenewalUpdater.executing(playerRenewal);
            playerFinder.findPlayer(tag).ifPresentOrElse(
                    playerRenewalProcessor::renewPlayer,
                    () -> playerFinder.findUnknownPlayer(tag).ifPresentOrElse(
                            playerRenewalProcessor::renewNewPlayer,
                            () -> {
                                throw new WorkerException("플레이어 정보가 존재하지 않습니다. tag=" + tag);
                            }
                    )
            );

            playerRenewalUpdater.complete(playerRenewal);
        } catch (BrawlStarsClientException.InMaintenance e) {
            playerRenewalUpdater.inMaintenance(playerRenewal);
        } catch (OptimisticLockingFailureException e) {
            throw new WorkerException("플레이어가 이미 갱신 중입니다.", e);
        } catch (Exception e) {
            playerRenewalUpdater.failed(playerRenewal);
            throw e;
        }
    }

}

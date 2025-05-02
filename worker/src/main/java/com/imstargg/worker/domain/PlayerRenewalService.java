package com.imstargg.worker.domain;

import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.client.brawlstars.BrawlStarsClientException;
import com.imstargg.client.brawlstars.response.BattleResponse;
import com.imstargg.client.brawlstars.response.ListResponse;
import com.imstargg.client.brawlstars.response.PlayerResponse;
import com.imstargg.worker.error.WorkerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
public class PlayerRenewalService {

    private static final Logger log = LoggerFactory.getLogger(PlayerRenewalService.class);

    private final BrawlStarsClient brawlStarsClient;
    private final PlayerRenewalRepository playerRenewalRepository;
    private final PlayerRepository playerRepository;

    public PlayerRenewalService(
            BrawlStarsClient brawlStarsClient,
            PlayerRenewalRepository playerRenewalRepository,
            PlayerRepository playerRepository
    ) {
        this.brawlStarsClient = brawlStarsClient;
        this.playerRenewalRepository = playerRenewalRepository;
        this.playerRepository = playerRepository;
    }

    public void renew(String tag) {
        if (!playerRenewalRepository.canRenew(tag)) {
            throw new WorkerException("플레이어 갱신이 불가능합니다. tag=" + tag);
        }

        try {
            playerRenewalRepository.executing(tag);
            PlayerResponse playerResponse = brawlStarsClient.getPlayerInformation(tag);
            ListResponse<BattleResponse> battleListResponse = brawlStarsClient.getPlayerRecentBattles(tag);

            if (playerRepository.existsPlayer(tag)) {
                log.info("플레이어 갱신 tag={}", tag);
                playerRepository.update(playerResponse, battleListResponse);
            } else if (playerRepository.existsUnknownPlayer(tag)) {
                log.info("신규 플레이어 갱신 tag={}", tag);
                playerRepository.create(playerResponse, battleListResponse);
            } else {
                throw new IllegalStateException("플레이어 정보가 존재하지 않습니다. tag=" + tag);
            }

        } catch (BrawlStarsClientException.InMaintenance e) {
            playerRenewalRepository.inMaintenance(tag);
        } catch (BrawlStarsClientException.NotFound e) {
            playerRepository.delete(tag);
        } catch (OptimisticLockingFailureException e) {
            log.info("플레이어 갱신 중 충돌 발생. tag={}", tag);
        } catch (Exception e) {
            playerRenewalRepository.failed(tag);
            throw e;
        }
    }

}

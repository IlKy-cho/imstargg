package com.imstargg.core.domain.player;

import com.imstargg.core.domain.BrawlStarsTag;
import com.imstargg.core.error.CoreErrorType;
import com.imstargg.core.error.CoreException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    private final PlayerReader playerReader;
    private final PlayerFinder playerFinder;
    private final PlayerRenewalExecutor playerRenewalExecutor;

    public PlayerService(
            PlayerReader playerReader,
            PlayerFinder playerFinder,
            PlayerRenewalExecutor playerRenewalExecutor
    ) {
        this.playerReader = playerReader;
        this.playerFinder = playerFinder;
        this.playerRenewalExecutor = playerRenewalExecutor;
    }

    public Player get(BrawlStarsTag tag) {
        return playerReader.get(tag);
    }

    public List<PlayerBrawler> getBrawlers(BrawlStarsTag tag) {
        return playerReader.getBrawlers(playerReader.get(tag));
    }

    public void renew(BrawlStarsTag tag) {
        Player player = playerFinder.find(tag)
                .orElseThrow(() -> new CoreException(CoreErrorType.PLAYER_NOT_FOUND, "playerTag=" + tag));
        playerRenewalExecutor.renew(player);
    }

    public void renewNew(BrawlStarsTag tag) {
        if (playerFinder.find(tag).isPresent()) {
            // 새 플레이어 갱신 기능은 없는 플레이어를 추가하는 것이 목적이므로,
            // 플레이어 존재 시 굳이 예외를 발생시키지 않는다. -> 예외로 인한 클라이언트 분기처리 불필요
            return;
        }
        playerRenewalExecutor.renewNew(tag);
    }

    public boolean isRenewing(BrawlStarsTag tag) {
        return playerRenewalExecutor.isRenewing(playerReader.get(tag));
    }

    public boolean isRenewingNew(BrawlStarsTag tag) {
        return playerRenewalExecutor.isRenewingNew(tag);
    }

}

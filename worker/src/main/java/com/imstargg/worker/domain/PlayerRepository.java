package com.imstargg.worker.domain;

import com.imstargg.client.brawlstars.response.BattleResponse;
import com.imstargg.client.brawlstars.response.ListResponse;
import com.imstargg.client.brawlstars.response.PlayerResponse;
import com.imstargg.collection.domain.PlayerBattleUpdateApplier;
import com.imstargg.collection.domain.PlayerUpdateApplier;
import com.imstargg.storage.db.core.player.BattleCollectionEntity;
import com.imstargg.storage.db.core.player.BattleCollectionJpaRepository;
import com.imstargg.storage.db.core.player.PlayerCollectionEntity;
import com.imstargg.storage.db.core.player.PlayerCollectionJpaRepository;
import com.imstargg.storage.db.core.player.UnknownPlayerCollectionEntity;
import com.imstargg.storage.db.core.player.UnknownPlayerCollectionJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlayerRepository {

    private final PlayerRenewalRepository playerRenewalRepository;
    private final PlayerCollectionJpaRepository playerJpaRepository;
    private final BattleCollectionJpaRepository battleJpaRepository;
    private final UnknownPlayerCollectionJpaRepository unknownPlayerJpaRepository;
    private final PlayerUpdateApplier playerUpdateApplier;
    private final PlayerBattleUpdateApplier playerBattleUpdateApplier;

    public PlayerRepository(
            PlayerRenewalRepository playerRenewalRepository,
            PlayerCollectionJpaRepository playerJpaRepository,
            BattleCollectionJpaRepository battleJpaRepository,
            UnknownPlayerCollectionJpaRepository unknownPlayerJpaRepository,
            PlayerUpdateApplier playerUpdateApplier,
            PlayerBattleUpdateApplier playerBattleUpdateApplier
    ) {
        this.playerRenewalRepository = playerRenewalRepository;
        this.playerJpaRepository = playerJpaRepository;
        this.battleJpaRepository = battleJpaRepository;
        this.unknownPlayerJpaRepository = unknownPlayerJpaRepository;
        this.playerUpdateApplier = playerUpdateApplier;
        this.playerBattleUpdateApplier = playerBattleUpdateApplier;
    }

    public boolean existsPlayer(String brawlStarsTag) {
        return playerJpaRepository.existsByBrawlStarsTag(brawlStarsTag);
    }

    public boolean existsUnknownPlayer(String brawlStarsTag) {
        return unknownPlayerJpaRepository.existsByBrawlStarsTag(brawlStarsTag);
    }

    @Transactional
    public void create(PlayerResponse playerResponse, ListResponse<BattleResponse> battleListResponse) {
        unknownPlayerJpaRepository.findOptimisticLockByBrawlStarsTag(playerResponse.tag())
                .orElseThrow(() -> notFoundException(playerResponse.tag()))
                .delete();

        PlayerCollectionEntity player = playerUpdateApplier.create(playerResponse);
        List<BattleCollectionEntity> battles = playerBattleUpdateApplier.update(player, battleListResponse);

        playerJpaRepository.save(player);
        battleJpaRepository.saveAll(battles);
        playerRenewalRepository.complete(playerResponse.tag());
    }

    @Transactional
    public void update(PlayerResponse playerResponse, ListResponse<BattleResponse> battleListResponse) {
        PlayerCollectionEntity player = playerJpaRepository
                .findWithOptimisticLockAndBrawlersByBrawlStarsTag(playerResponse.tag())
                .orElseThrow(() -> notFoundException(playerResponse.tag()));
        playerUpdateApplier.update(player, playerResponse);
        List<BattleCollectionEntity> battles = playerBattleUpdateApplier.update(player, battleListResponse);

        playerJpaRepository.save(player);
        battleJpaRepository.saveAll(battles);
        playerRenewalRepository.complete(playerResponse.tag());
    }

    @Transactional
    public void delete(String brawlStarsTag) {
        playerJpaRepository.findWithOptimisticLockAndBrawlersByBrawlStarsTag(brawlStarsTag).ifPresentOrElse(
                PlayerCollectionEntity::deleted,
                () -> unknownPlayerJpaRepository.findOptimisticLockByBrawlStarsTag(brawlStarsTag).ifPresentOrElse(
                        UnknownPlayerCollectionEntity::notFound,
                        () -> {
                            throw notFoundException(brawlStarsTag);
                        }
                )
        );
        playerRenewalRepository.complete(brawlStarsTag);
    }

    private IllegalStateException notFoundException(String brawlStarsTag) {
        return new IllegalStateException("플레이어 정보가 존재하지 않습니다. tag=" + brawlStarsTag);
    }
}

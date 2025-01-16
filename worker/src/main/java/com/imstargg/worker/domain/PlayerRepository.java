package com.imstargg.worker.domain;

import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattleCollectionJpaRepository;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import com.imstargg.storage.db.core.PlayerCollectionJpaRepository;
import com.imstargg.storage.db.core.UnknownPlayerCollectionEntity;
import com.imstargg.storage.db.core.UnknownPlayerCollectionJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class PlayerRepository {

    private final PlayerCollectionJpaRepository playerJpaRepository;
    private final BattleCollectionJpaRepository battleJpaRepository;
    private final UnknownPlayerCollectionJpaRepository unknownPlayerJpaRepository;

    public PlayerRepository(
            PlayerCollectionJpaRepository playerJpaRepository,
            BattleCollectionJpaRepository battleJpaRepository,
            UnknownPlayerCollectionJpaRepository unknownPlayerJpaRepository
    ) {
        this.playerJpaRepository = playerJpaRepository;
        this.battleJpaRepository = battleJpaRepository;
        this.unknownPlayerJpaRepository = unknownPlayerJpaRepository;
    }

    @Transactional
    public Optional<PlayerCollectionEntity> find(String brawlStarsTag) {
        return playerJpaRepository.findWithOptimisticLockByBrawlStarsTag(brawlStarsTag)
                .map(player -> {
                    player.initializeBrawlStarsIdToBrawler();
                    return player;
                });
    }

    @Transactional
    public Optional<UnknownPlayerCollectionEntity> findUnknown(String brawlStarsTag) {
        return unknownPlayerJpaRepository.findWithOptimisticLockByBrawlStarsTag(brawlStarsTag);
    }

    public PlayerCollectionEntity save(PlayerCollectionEntity player) {
        return playerJpaRepository.save(player);
    }

    @Transactional
    public void update(PlayerCollectionEntity player, List<BattleCollectionEntity> battles) {
        playerJpaRepository.save(player);
        battleJpaRepository.saveAll(battles);
    }

    @Transactional
    public void update(UnknownPlayerCollectionEntity unknownPlayer) {
        unknownPlayerJpaRepository.save(unknownPlayer);
    }

    @Transactional
    public void update(
            UnknownPlayerCollectionEntity unknownPlayer,
            PlayerCollectionEntity player,
            List<BattleCollectionEntity> battles
    ) {
        unknownPlayerJpaRepository.delete(unknownPlayer);
        playerJpaRepository.save(player);
        battleJpaRepository.saveAll(battles);
    }

}

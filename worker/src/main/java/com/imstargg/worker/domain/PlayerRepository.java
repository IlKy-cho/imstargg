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

    private final PlayerCollectionJpaRepository playerRepository;
    private final BattleCollectionJpaRepository battleRepository;
    private final UnknownPlayerCollectionJpaRepository unknownPlayerRepository;

    public PlayerRepository(
            PlayerCollectionJpaRepository playerRepository,
            BattleCollectionJpaRepository battleRepository,
            UnknownPlayerCollectionJpaRepository unknownPlayerRepository
    ) {
        this.playerRepository = playerRepository;
        this.battleRepository = battleRepository;
        this.unknownPlayerRepository = unknownPlayerRepository;
    }

    public Optional<PlayerCollectionEntity> find(String brawlStarsTag) {
        return playerRepository.findWithOptimisticLockByBrawlStarsTag(brawlStarsTag);
    }

    public Optional<UnknownPlayerCollectionEntity> findUnknown(String brawlStarsTag) {
        return unknownPlayerRepository.findWithOptimisticLockByBrawlStarsTag(brawlStarsTag);
    }

    @Transactional
    public void update(PlayerCollectionEntity player) {
        playerRepository.save(player);
    }

    @Transactional
    public void update(PlayerCollectionEntity player, List<BattleCollectionEntity> battles) {
        playerRepository.save(player);
        battleRepository.saveAll(battles);
    }

    @Transactional
    public void update(UnknownPlayerCollectionEntity unknownPlayer) {
        unknownPlayerRepository.save(unknownPlayer);
    }

    @Transactional
    public void update(
            UnknownPlayerCollectionEntity unknownPlayer,
            PlayerCollectionEntity player,
            List<BattleCollectionEntity> battles
    ) {
        unknownPlayerRepository.save(unknownPlayer);
        playerRepository.save(player);
        battleRepository.saveAll(battles);
    }

    public PlayerCollectionEntity add(PlayerCollectionEntity player) {
        return playerRepository.save(player);
    }
}

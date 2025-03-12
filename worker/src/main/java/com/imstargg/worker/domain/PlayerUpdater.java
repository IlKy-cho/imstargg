package com.imstargg.worker.domain;

import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattleCollectionJpaRepository;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import com.imstargg.storage.db.core.PlayerCollectionJpaRepository;
import com.imstargg.storage.db.core.UnknownPlayerCollectionEntity;
import com.imstargg.storage.db.core.UnknownPlayerCollectionJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlayerUpdater {

    private final PlayerCollectionJpaRepository playerJpaRepository;
    private final BattleCollectionJpaRepository battleJpaRepository;
    private final UnknownPlayerCollectionJpaRepository unknownPlayerJpaRepository;

    public PlayerUpdater(
            PlayerCollectionJpaRepository playerJpaRepository,
            BattleCollectionJpaRepository battleJpaRepository,
            UnknownPlayerCollectionJpaRepository unknownPlayerJpaRepository
    ) {
        this.playerJpaRepository = playerJpaRepository;
        this.battleJpaRepository = battleJpaRepository;
        this.unknownPlayerJpaRepository = unknownPlayerJpaRepository;
    }

    public void update(UnknownPlayerCollectionEntity unknownPlayer) {
        unknownPlayerJpaRepository.save(unknownPlayer);
    }

    public void update(PlayerCollectionEntity player, List<BattleCollectionEntity> battles) {
        playerJpaRepository.save(player);
        battleJpaRepository.saveAll(battles);
    }

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

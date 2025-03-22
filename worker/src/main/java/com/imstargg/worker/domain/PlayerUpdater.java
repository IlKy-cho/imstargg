package com.imstargg.worker.domain;

import com.imstargg.storage.db.core.player.BattleCollectionEntity;
import com.imstargg.storage.db.core.player.BattleCollectionJpaRepository;
import com.imstargg.storage.db.core.player.PlayerCollectionEntity;
import com.imstargg.storage.db.core.player.PlayerCollectionJpaRepository;
import com.imstargg.storage.db.core.player.UnknownPlayerCollectionEntity;
import com.imstargg.storage.db.core.player.UnknownPlayerCollectionJpaRepository;
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
        unknownPlayerJpaRepository.save(unknownPlayer);
        playerJpaRepository.save(player);
        battleJpaRepository.saveAll(battles);
    }
}

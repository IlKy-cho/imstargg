package com.imstargg.worker.domain;

import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattleCollectionJpaRepository;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import com.imstargg.storage.db.core.PlayerCollectionJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class PlayerRepository {

    private final PlayerCollectionJpaRepository playerRepository;
    private final BattleCollectionJpaRepository battleRepository;

    public PlayerRepository(
            PlayerCollectionJpaRepository playerRepository,
            BattleCollectionJpaRepository battleRepository
    ) {
        this.playerRepository = playerRepository;
        this.battleRepository = battleRepository;
    }

    public Optional<PlayerCollectionEntity> find(String brawlStarsTag) {
        return playerRepository.findWithOptimisticLockByBrawlStarsTag(brawlStarsTag);
    }

    @Transactional
    public void update(PlayerCollectionEntity player, List<BattleCollectionEntity> battles) {
        playerRepository.save(player);
        battleRepository.saveAll(battles);
    }
}

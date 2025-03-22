package com.imstargg.admin.domain;

import com.imstargg.storage.db.core.player.BattleCollectionEntity;
import com.imstargg.storage.db.core.player.BattleCollectionJpaRepository;
import com.imstargg.storage.db.core.player.PlayerCollectionEntity;
import com.imstargg.storage.db.core.player.PlayerCollectionJpaRepository;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService {

    private final BattleCollectionJpaRepository battleCollectionJpaRepository;
    private final PlayerCollectionJpaRepository playerCollectionJpaRepository;

    public StatisticsService(
            BattleCollectionJpaRepository battleCollectionJpaRepository,
            PlayerCollectionJpaRepository playerCollectionJpaRepository
    ) {
        this.battleCollectionJpaRepository = battleCollectionJpaRepository;
        this.playerCollectionJpaRepository = playerCollectionJpaRepository;
    }

    public long getBattleLatestId() {
        return battleCollectionJpaRepository.findFirst1ByOrderByIdDesc()
                .map(BattleCollectionEntity::getId)
                .orElse(0L);
    }

    @Nullable
    public long getPlayerLatestId() {
        return playerCollectionJpaRepository.findFirst1ByOrderByIdDesc()
                .map(PlayerCollectionEntity::getId)
                .orElse(0L);
    }
}

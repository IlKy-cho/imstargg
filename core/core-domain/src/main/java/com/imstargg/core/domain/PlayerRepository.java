package com.imstargg.core.domain;

import com.imstargg.storage.db.core.PlayerEntity;
import com.imstargg.storage.db.core.PlayerJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PlayerRepository {

    private final PlayerJpaRepository playerJpaRepository;

    public PlayerRepository(PlayerJpaRepository playerJpaRepository) {
        this.playerJpaRepository = playerJpaRepository;
    }

    public Optional<Player> findByTag(BrawlStarsTag tag) {
        return playerJpaRepository.findByBrawlStarsTagAndDeletedFalse(tag.value())
                .map(this::mapEntityToPlayer);
    }

    public List<Player> findByName(String name) {
        return playerJpaRepository.findAllByNameAndDeletedFalse(name).stream()
                .map(this::mapEntityToPlayer)
                .toList();
    }

    private Player mapEntityToPlayer(PlayerEntity entity) {
        return new Player(
                new BrawlStarsTag(entity.getBrawlStarsTag()),
                entity.getName(),
                entity.getNameColor(),
                entity.getIconBrawlStarsId(),
                entity.getTrophies(),
                entity.getHighestTrophies(),
                entity.getBrawlStarsClubTag() == null ? null : new BrawlStarsTag(entity.getBrawlStarsClubTag()),
                entity.getUpdatedAt()
        );
    }
}

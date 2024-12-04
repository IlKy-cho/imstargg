package com.imstargg.core.domain;

import com.imstargg.storage.db.core.PlayerEntity;
import com.imstargg.storage.db.core.PlayerJpaRepository;
import com.imstargg.storage.db.core.UnknownPlayerEntity;
import com.imstargg.storage.db.core.UnknownPlayerJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;
import java.util.Optional;

@Component
public class PlayerRepository {

    private final Clock clock;

    private final PlayerJpaRepository playerJpaRepository;

    private final UnknownPlayerJpaRepository unknownPlayerJpaRepository;

    public PlayerRepository(
            Clock clock,
            PlayerJpaRepository playerJpaRepository,
            UnknownPlayerJpaRepository unknownPlayerJpaRepository
    ) {
        this.clock = clock;
        this.playerJpaRepository = playerJpaRepository;
        this.unknownPlayerJpaRepository = unknownPlayerJpaRepository;
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

    @Transactional
    public NewPlayer getNew(BrawlStarsTag tag) {
        UnknownPlayerEntity entity = unknownPlayerJpaRepository.findByBrawlStarsTag(tag.value())
                .orElseGet(() -> unknownPlayerJpaRepository.save(
                        UnknownPlayerEntity.newSearchNew(
                                tag.value(),
                                clock
                        )
                ));
        entity.restore();
        entity.refresh(clock);
        return new NewPlayer(
                tag,
                entity.getStatus(),
                entity.getUpdateAvailableAt()
        );
    }
}

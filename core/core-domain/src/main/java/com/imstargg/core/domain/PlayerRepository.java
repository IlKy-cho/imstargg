package com.imstargg.core.domain;

import com.imstargg.core.enums.Brawler;
import com.imstargg.core.enums.Gadget;
import com.imstargg.core.enums.Gear;
import com.imstargg.core.enums.StarPower;
import com.imstargg.core.error.CoreException;
import com.imstargg.storage.db.core.BaseEntity;
import com.imstargg.storage.db.core.PlayerBrawlerEntity;
import com.imstargg.storage.db.core.PlayerBrawlerJpaRepository;
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

    private final PlayerBrawlerJpaRepository playerBrawlerJpaRepository;

    public PlayerRepository(
            Clock clock,
            PlayerJpaRepository playerJpaRepository,
            UnknownPlayerJpaRepository unknownPlayerJpaRepository,
            PlayerBrawlerJpaRepository playerBrawlerJpaRepository
    ) {
        this.clock = clock;
        this.playerJpaRepository = playerJpaRepository;
        this.unknownPlayerJpaRepository = unknownPlayerJpaRepository;
        this.playerBrawlerJpaRepository = playerBrawlerJpaRepository;
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
                new PlayerId(entity.getId()),
                new BrawlStarsTag(entity.getBrawlStarsTag()),
                entity.getName(),
                entity.getNameColor(),
                entity.getIconBrawlStarsId(),
                entity.getTrophies(),
                entity.getHighestTrophies(),
                entity.getBrawlStarsClubTag() == null ? null : new BrawlStarsTag(entity.getBrawlStarsClubTag()),
                entity.getUpdatedAt(),
                entity.getStatus()
        );
    }

    @Transactional
    public NewPlayer getNew(BrawlStarsTag tag) {
        UnknownPlayerEntity entity = unknownPlayerJpaRepository.findByBrawlStarsTag(tag.value())
                .filter(BaseEntity::isActive)
                .orElseGet(() -> unknownPlayerJpaRepository.save(
                        UnknownPlayerEntity.newSearchNew(
                                tag.value(),
                                clock
                        )
                ));
        return mapNewPlayer(entity);
    }

    public Optional<NewPlayer> findNew(BrawlStarsTag tag) {
        return unknownPlayerJpaRepository.findByBrawlStarsTag(tag.value())
                .map(PlayerRepository::mapNewPlayer);
    }

    private static NewPlayer mapNewPlayer(UnknownPlayerEntity entity) {
        return new NewPlayer(
                new BrawlStarsTag(entity.getBrawlStarsTag()),
                entity.getStatus(),
                entity.getUpdateAvailableAt()
        );
    }

    public List<PlayerBrawler> findBrawlers(Player player) {
        PlayerEntity playerEntity = playerJpaRepository.findByBrawlStarsTagAndDeletedFalse(player.tag().value())
                .orElseThrow(() -> new CoreException("Player not found: " + player.tag()));
        return playerBrawlerJpaRepository.findAllByPlayerId(playerEntity.getId()).stream()
                .map(this::mapEntityToPlayerBrawler)
                .toList();
    }

    private PlayerBrawler mapEntityToPlayerBrawler(PlayerBrawlerEntity entity) {
        return new PlayerBrawler(
                Brawler.find(entity.getBrawlerBrawlStarsId()),
                entity.getGearBrawlStarsIds().stream()
                        .map(Gear::find).toList(),
                entity.getStarPowerBrawlStarsIds().stream()
                        .map(StarPower::find).toList(),
                entity.getGadgetBrawlStarsIds().stream()
                        .map(Gadget::find).toList(),
                entity.getPower(),
                entity.getRank(),
                entity.getTrophies(),
                entity.getHighestTrophies()
        );
    }

    @Transactional
    public void renewRequested(Player player) {
        PlayerEntity playerEntity = playerJpaRepository.findById(player.id().value())
                .orElseThrow(() -> new CoreException("Player not found. id=" + player.id()));
        playerEntity.renewRequested();
    }
}

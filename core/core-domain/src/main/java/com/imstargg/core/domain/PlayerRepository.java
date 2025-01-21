package com.imstargg.core.domain;

import com.imstargg.core.enums.SoloRankTier;
import com.imstargg.core.error.CoreException;
import com.imstargg.storage.db.core.PlayerBrawlerEntity;
import com.imstargg.storage.db.core.PlayerBrawlerJpaRepository;
import com.imstargg.storage.db.core.PlayerEntity;
import com.imstargg.storage.db.core.PlayerJpaRepository;
import com.imstargg.storage.db.core.UnknownPlayerEntity;
import com.imstargg.storage.db.core.UnknownPlayerJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PlayerRepository {

    private final PlayerJpaRepository playerJpaRepository;
    private final UnknownPlayerJpaRepository unknownPlayerJpaRepository;
    private final PlayerBrawlerJpaRepository playerBrawlerJpaRepository;

    public PlayerRepository(
            PlayerJpaRepository playerJpaRepository,
            UnknownPlayerJpaRepository unknownPlayerJpaRepository,
            PlayerBrawlerJpaRepository playerBrawlerJpaRepository
    ) {
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
                entity.getSoloRankTier() == null ? null : SoloRankTier.of(entity.getSoloRankTier()),
                entity.getBrawlStarsClubTag() == null ? null : new BrawlStarsTag(entity.getBrawlStarsClubTag()),
                entity.getUpdatedAt().toLocalDateTime(),
                entity.getStatus()
        );
    }

    public UnknownPlayer getUnknown(BrawlStarsTag tag) {
        UnknownPlayerEntity entity = unknownPlayerJpaRepository.findByBrawlStarsTag(tag.value())
                .orElseGet(() -> unknownPlayerJpaRepository.save(new UnknownPlayerEntity(tag.value())));
        return mapNewPlayer(entity);
    }


    private UnknownPlayer mapNewPlayer(UnknownPlayerEntity entity) {
        return new UnknownPlayer(
                new BrawlStarsTag(entity.getBrawlStarsTag()),
                entity.getNotFoundCount(),
                entity.getUpdatedAt().toLocalDateTime()
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
                new BrawlStarsId(entity.getBrawlerBrawlStarsId()),
                entity.getGearBrawlStarsIds().stream().map(BrawlStarsId::new).toList(),
                entity.getStarPowerBrawlStarsIds().stream().map(BrawlStarsId::new).toList(),
                entity.getGadgetBrawlStarsIds().stream().map(BrawlStarsId::new).toList(),
                entity.getPower(),
                entity.getRank(),
                entity.getTrophies(),
                entity.getHighestTrophies()
        );
    }
}

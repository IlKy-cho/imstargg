package com.imstargg.core.domain;

import com.imstargg.core.domain.brawlstars.Brawler;
import com.imstargg.core.domain.brawlstars.BrawlerRepositoryWithCache;
import com.imstargg.core.enums.Language;
import com.imstargg.core.enums.PlayerStatus;
import com.imstargg.core.enums.SoloRankTier;
import com.imstargg.core.enums.UnknownPlayerStatus;
import com.imstargg.core.error.CoreException;
import com.imstargg.storage.db.core.PlayerBrawlerEntity;
import com.imstargg.storage.db.core.PlayerBrawlerJpaRepository;
import com.imstargg.storage.db.core.PlayerEntity;
import com.imstargg.storage.db.core.PlayerJpaRepository;
import com.imstargg.storage.db.core.UnknownPlayerEntity;
import com.imstargg.storage.db.core.UnknownPlayerJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class PlayerRepository {

    private final Clock clock;
    private final PlayerJpaRepository playerJpaRepository;
    private final UnknownPlayerJpaRepository unknownPlayerJpaRepository;
    private final PlayerBrawlerJpaRepository playerBrawlerJpaRepository;
    private final BrawlerRepositoryWithCache brawlerRepository;

    public PlayerRepository(
            Clock clock,
            PlayerJpaRepository playerJpaRepository,
            UnknownPlayerJpaRepository unknownPlayerJpaRepository,
            PlayerBrawlerJpaRepository playerBrawlerJpaRepository,
            BrawlerRepositoryWithCache brawlerRepository
    ) {
        this.clock = clock;
        this.playerJpaRepository = playerJpaRepository;
        this.unknownPlayerJpaRepository = unknownPlayerJpaRepository;
        this.playerBrawlerJpaRepository = playerBrawlerJpaRepository;
        this.brawlerRepository = brawlerRepository;
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
                entity.getUpdatedAt(),
                entity.getStatus()
        );
    }

    public int countRenewRequested() {
        return unknownPlayerJpaRepository.countByStatus(UnknownPlayerStatus.SEARCH_NEW)
                + playerJpaRepository.countByStatus(PlayerStatus.RENEW_REQUESTED);
    }

    public UnknownPlayer getUnknown(BrawlStarsTag tag) {
        UnknownPlayerEntity entity = unknownPlayerJpaRepository.findByBrawlStarsTag(tag.value())
                .orElseGet(() -> unknownPlayerJpaRepository.save(
                        UnknownPlayerEntity.newSearchNew(
                                tag.value(),
                                clock
                        )
                ));
        return mapNewPlayer(entity);
    }

    @Transactional
    public void updateSearchNew(UnknownPlayer unknownPlayer) {
        UnknownPlayerEntity entity = unknownPlayerJpaRepository.findByBrawlStarsTag(unknownPlayer.tag().value())
                .orElseThrow(() -> new CoreException("Unknown player not found: " + unknownPlayer.tag()));
        entity.searchNew();
    }

    public Optional<UnknownPlayer> findNew(BrawlStarsTag tag) {
        return unknownPlayerJpaRepository.findByBrawlStarsTag(tag.value())
                .map(this::mapNewPlayer);
    }

    private UnknownPlayer mapNewPlayer(UnknownPlayerEntity entity) {
        return new UnknownPlayer(
                new BrawlStarsTag(entity.getBrawlStarsTag()),
                entity.getStatus(),
                updateAvailableAt(entity)
        );
    }

    private LocalDateTime updateAvailableAt(UnknownPlayerEntity entity) {
        if (entity.getStatus() != UnknownPlayerStatus.NOT_FOUND) {
            return LocalDateTime.now(clock);
        }

        return entity.getUpdatedAt().plusMinutes(1L + entity.getNotFoundCount());
    }

    public List<PlayerBrawler> findBrawlers(Player player) {
        PlayerEntity playerEntity = playerJpaRepository.findByBrawlStarsTagAndDeletedFalse(player.tag().value())
                .orElseThrow(() -> new CoreException("Player not found: " + player.tag()));
        return playerBrawlerJpaRepository.findAllByPlayerId(playerEntity.getId()).stream()
                .map(this::mapEntityToPlayerBrawler)
                .toList();
    }

    private PlayerBrawler mapEntityToPlayerBrawler(PlayerBrawlerEntity entity) {
        Optional<Brawler> brawlerOpt = brawlerRepository.find(
                new BrawlStarsId(entity.getBrawlerBrawlStarsId()), Language.KOREAN);
        if (brawlerOpt.isEmpty()) {
            return new PlayerBrawler(
                    null,
                    List.of(),
                    List.of(),
                    List.of(),
                    entity.getPower(),
                    entity.getRank(),
                    entity.getTrophies(),
                    entity.getHighestTrophies()
            );
        }

        Brawler brawler = brawlerOpt.get();

        return new PlayerBrawler(
                brawlerOpt.orElse(null),
                brawler.filterGears(entity.getGearBrawlStarsIds().stream().map(BrawlStarsId::new).toList()),
                brawler.filterStarPowers(entity.getStarPowerBrawlStarsIds().stream().map(BrawlStarsId::new).toList()),
                brawler.filterGadgets(entity.getGadgetBrawlStarsIds().stream().map(BrawlStarsId::new).toList()),
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

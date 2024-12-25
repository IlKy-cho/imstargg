package com.imstargg.core.domain;

import com.imstargg.core.domain.brawlstars.BattleEventRepositoryWithCache;
import com.imstargg.core.domain.brawlstars.BrawlerRepositoryWithCache;
import com.imstargg.core.enums.BattleResult;
import com.imstargg.core.enums.BattleType;
import com.imstargg.core.enums.Language;
import com.imstargg.core.enums.SoloRankTier;
import com.imstargg.core.error.CoreException;
import com.imstargg.storage.db.core.BattleEntity;
import com.imstargg.storage.db.core.BattleEntityTeamPlayer;
import com.imstargg.storage.db.core.BattleJpaRepository;
import com.imstargg.storage.db.core.PlayerEntity;
import com.imstargg.storage.db.core.PlayerJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BattleRepository {

    private static final int PAGE_SIZE = 25;

    private final PlayerJpaRepository playerJpaRepository;
    private final BattleJpaRepository battleJpaRepository;
    private final BattleEventRepositoryWithCache battleEventRepository;
    private final BrawlerRepositoryWithCache brawlerRepository;

    public BattleRepository(
            PlayerJpaRepository playerJpaRepository,
            BattleJpaRepository battleJpaRepository,
            BattleEventRepositoryWithCache battleEventRepository,
            BrawlerRepositoryWithCache brawlerRepository
    ) {
        this.playerJpaRepository = playerJpaRepository;
        this.battleJpaRepository = battleJpaRepository;
        this.battleEventRepository = battleEventRepository;
        this.brawlerRepository = brawlerRepository;
    }

    public Slice<PlayerBattle> find(Player player, int page) {
        PlayerEntity playerEntity = playerJpaRepository.findByBrawlStarsTagAndDeletedFalse(player.tag().value())
                .orElseThrow(() -> new CoreException("Player not found: " + player.tag()));
        var battleEntities = battleJpaRepository
                .findAllByPlayerPlayerIdAndDeletedFalseOrderByBattleTimeDesc(
                        playerEntity.getId(), PageRequest.of(page, PAGE_SIZE));
        return new Slice<>(battleEntities.map(this::mapBattle).toList(), battleEntities.hasNext());
    }

    private PlayerBattle mapBattle(BattleEntity battleEntity) {
        BattleType battleType = BattleType.find(battleEntity.getType());
        return new PlayerBattle(
                battleEntity.getBattleTime(),
                battleEventRepository.find(
                                battleEntity.getEvent().getEventBrawlStarsId() != null
                                        ? new BrawlStarsId(battleEntity.getEvent().getEventBrawlStarsId()) : null,
                                Language.KOREAN)
                        .orElse(null),
                battleType,
                battleEntity.getResult() != null ? BattleResult.map(battleEntity.getResult()) : null,
                battleEntity.getDuration(),
                battleEntity.getPlayer().getRank(),
                battleEntity.getPlayer().getTrophyChange(),
                new BrawlStarsTag(battleEntity.getStarPlayerBrawlStarsTag()),
                mapTeams(battleType, battleEntity.getTeams())
        );
    }

    private List<List<BattlePlayer>> mapTeams(
            BattleType battleType, List<List<BattleEntityTeamPlayer>> teams) {
        return teams.stream()
                .map(team -> team.stream()
                        .map(player -> mapPlayer(battleType, player)).toList()
                ).toList();
    }

    private BattlePlayer mapPlayer(BattleType battleType, BattleEntityTeamPlayer player) {
        return new BattlePlayer(
                new BrawlStarsTag(player.getBrawlStarsTag()),
                player.getName(),
                getSoloRankTier(battleType, player),
                mapBattlePlayerBrawler(battleType, player)
        );
    }

    private SoloRankTier getSoloRankTier(BattleType battleType, BattleEntityTeamPlayer player) {
        if (BattleType.SOLO_RANKED.equals(battleType) && player.getBrawler().getTrophies() != null) {
            return SoloRankTier.of(player.getBrawler().getTrophies());
        }
        return null;
    }

    private BattlePlayerBrawler mapBattlePlayerBrawler(BattleType battleType, BattleEntityTeamPlayer player) {
        boolean isSoloRanked = BattleType.SOLO_RANKED.equals(battleType);
        return new BattlePlayerBrawler(
                brawlerRepository.find(
                        new BrawlStarsId(player.getBrawler().getBrawlStarsId()),
                        Language.KOREAN
                ).orElse(null),
                player.getBrawler().getPower(),
                isSoloRanked ? null : player.getBrawler().getTrophies(),
                isSoloRanked ? null : player.getBrawler().getTrophyChange()
        );
    }
}
